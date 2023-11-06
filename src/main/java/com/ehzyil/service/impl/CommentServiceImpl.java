package com.ehzyil.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.*;
import com.ehzyil.mapper.ArticleMapper;
import com.ehzyil.mapper.CommentMapper;
import com.ehzyil.mapper.TalkMapper;
import com.ehzyil.mapper.UserMapper;
import com.ehzyil.model.dto.CheckDTO;
import com.ehzyil.model.dto.CommentDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.MailDTO;
import com.ehzyil.model.vo.admin.CommentBackVO;
import com.ehzyil.model.vo.front.*;
import com.ehzyil.service.ICommentService;
import com.ehzyil.service.RedisService;
import com.ehzyil.utils.PageUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ehzyil.constant.CommentTypeEnum.*;
import static com.ehzyil.constant.CommonConstant.*;
import static com.ehzyil.constant.MqConstant.*;
import static com.ehzyil.constant.RedisConstant.COMMENT_LIKE_COUNT;
import static com.ehzyil.constant.RedisConstant.SITE_SETTING;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements ICommentService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Resource
    private TalkMapper talkMapper;
    @Value("${blog.url}")
    private String websiteUrl;

    @Override
    public List<RecentCommentVO> listRecentCommentVO() {
        return getBaseMapper().selectRecentCommentVOList();
    }

    @Override
    public PageResult<CommentVO> listCommentVO(ConditionDTO conditionDTO) {
        //查询数量
        Long count = getBaseMapper().selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Objects.nonNull(conditionDTO.getTypeId()), Comment::getTypeId, conditionDTO.getTypeId())
                .eq(Comment::getCommentType, conditionDTO.getCommentType())
                .eq(Comment::getIsCheck, 1)
                .isNull(Comment::getParentId));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询父评论
        List<CommentVO> commentVOList = getBaseMapper().selectParentComment(PageUtils.getLimit(), PageUtils.getSize(), conditionDTO);
        if (CollectionUtils.isEmpty(commentVOList)) {
            return new PageResult<>();
        }

        // 评论点赞
        Map<String, Integer> likeCountMap = redisService.getHashAll(COMMENT_LIKE_COUNT);
        //获取父评论ids
        List<Integer> parentCommentIdList = commentVOList.stream().map(CommentVO::getId).collect(Collectors.toList());
        // 分组查询每组父评论下的子评论前三条
        List<CommentReplyVO> commentReplyVOS = getBaseMapper().selectReplyByParentIdList(parentCommentIdList);
        // 封装子评论点赞量
        commentReplyVOS.forEach(commentReplyVO -> commentReplyVO.setLikeCount(Optional.ofNullable(likeCountMap.get(commentReplyVO.getId().toString())).orElse(0)));
        // 根据父评论id生成对应子评论的Map
        Map<Integer, List<CommentReplyVO>> replyMap = commentReplyVOS.stream().collect(Collectors.groupingBy(CommentReplyVO::getParentId));
        // 父评论的回复数量
        List<CommentReplyCountVO> commentReplyCountVOS = getBaseMapper().selectReplyCountByParentId(parentCommentIdList, String.valueOf(conditionDTO.getCommentType()));
        //转为map
        Map<Integer, Integer> commentReplyCountVOMap = commentReplyCountVOS.stream().collect(Collectors.toMap(CommentReplyCountVO::getCommentId, CommentReplyCountVO::getReplyCount));

        // 封装评论数据
        commentVOList.forEach(item -> {
            //评论点赞
            item.setLikeCount(Optional.ofNullable(likeCountMap.get(item.getId().toString())).orElse(0));
            //回复集合
            item.setReplyVOList(replyMap.get(item.getId()));
            //回复数量
            item.setReplyCount(Optional.ofNullable(commentReplyCountVOMap.get(item.getId())).orElse(0));
        });
        return new PageResult<>(commentVOList, count);
    }

    @Override
    public void addComment(CommentDTO commentDTO) {
        //拷贝对象
        Comment comment = BeanUtil.copyProperties(commentDTO, Comment.class);

        //设置回复用户id
        comment.setFromUid(StpUtil.getLoginIdAsInt());

        //获取网站配置
        SiteConfig siteConfig = redisService.getObject(SITE_SETTING);

        //设置is_check
        comment.setIsCheck(Optional.ofNullable(siteConfig.getCommentCheck()).orElse(0));

        //插入数据库
        getBaseMapper().insert(comment);

        // 查询评论用户昵称
        String fromNickname = userMapper.selectOne(new LambdaQueryWrapper<User>()
                        .select(User::getNickname)
                        .eq(User::getId, StpUtil.getLoginIdAsInt()))
                .getNickname();
        // 通知用户
        if (siteConfig.getEmailNotice().equals(TRUE)) {
            CompletableFuture.runAsync(() -> {
                notice(comment, fromNickname);
            });
        }

    }

    private Article getArticle(Integer articleId) {
        return articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getArticleTitle, Article::getUserId)
                .eq(Article::getId, articleId));
    }

    private Integer getTalkUserId(Integer talkId) {
        return talkMapper.selectOne(new LambdaQueryWrapper<Talk>()
                        .select(Talk::getUserId)
                        .eq(Talk::getId, talkId))
                .getUserId();
    }

    /**
     * 评论通知
     *
     * @param comment      评论
     * @param fromNickname 评论用户昵称
     */
    private void notice(Comment comment, String fromNickname) {

        // 自己回复自己不用提醒
        if (comment.getFromUid().equals(comment.getToUid())) {
            return;
        }

        // 邮件标题
        String title = "";
        // 回复用户id
        Integer toUid = -1;
        // 父评论
        if (Objects.isNull(comment.getParentId())) {
            //类型 (1文章 2友链 3说说)
            switch (comment.getCommentType()) {
                case 1:
                    Article article = getArticle(comment.getTypeId());
                    title = article.getArticleTitle();
                    toUid = article.getUserId();
                    break;
                case 2:
                    title = TALK.getDesc();
                    toUid = getTalkUserId(comment.getTypeId());
                    break;
                case 3:
                    title = LINK.getDesc();
                    toUid = getTalkUserId(comment.getTypeId());
                    break;
            }
        } else {
            // 子评论
            toUid = comment.getToUid();

            switch (comment.getCommentType()) {
                case 1:
                    Article article = getArticle(comment.getTypeId());
                    title = article.getArticleTitle();
                    break;
                case 2:
                    title = TALK.getDesc();
                    break;
                case 3:
                    title = LINK.getDesc();
                    break;
            }

        }
        // 查询回复用户邮箱、昵称、id
        User toUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .select(User::getEmail, User::getNickname)
                .eq(User::getId, toUid));
        // 邮箱不为空
        if (StringUtils.hasText(toUser.getEmail())) {
            sendEmail(comment, toUser, title, fromNickname);
        }

    }

    /**
     * 发送邮件
     *
     * @param comment      评论
     * @param toUser       回复用户信息
     * @param title        标题
     * @param fromNickname 评论用户昵称
     */
    private void sendEmail(Comment comment, User toUser, String title, String fromNickname) {
        MailDTO mailDTO = new MailDTO();
        if (comment.getIsCheck().equals(TRUE)) {
            Map<String, Object> contentMap = new HashMap<>(7);
            // 评论链接
            String typeId = Optional.ofNullable(comment.getTypeId())
                    .map(Object::toString)
                    .orElse("");
            String url = websiteUrl + getCommentPath(comment.getCommentType()) + typeId;

            // 父评论则提醒作者
            if (Objects.isNull(comment.getParentId())) {
                mailDTO.setToEmail(toUser.getEmail());
                mailDTO.setSubject(COMMENT_REMIND);
                mailDTO.setTemplate(AUTHOR_TEMPLATE);
                String createTime = DateUtil.formatLocalDateTime(comment.getCreateTime());
                contentMap.put("time", createTime);
                contentMap.put("url", url);
                contentMap.put("title", title);
                contentMap.put("nickname", fromNickname);
                contentMap.put("content", comment.getCommentContent());
                mailDTO.setContentMap(contentMap);
            } else {
                // 子评论则回复的是用户提醒该用户
                Comment parentComment = getBaseMapper().selectOne(new LambdaQueryWrapper<Comment>()
                        .select(Comment::getCommentContent, Comment::getCreateTime)
                        .eq(Comment::getId, comment.getReplyId()));
                mailDTO.setToEmail(toUser.getEmail());
                mailDTO.setSubject(COMMENT_REMIND);
                mailDTO.setTemplate(USER_TEMPLATE);
                contentMap.put("url", url);
                contentMap.put("title", title);
                String createTime = DateUtil.formatLocalDateTime(comment.getCreateTime());
                contentMap.put("time", createTime);
                // 被回复用户昵称
                contentMap.put("toUser", toUser.getNickname());
                // 评论用户昵称
                contentMap.put("fromUser", fromNickname);
                // 被回复的评论内容
                contentMap.put("parentComment", parentComment.getCommentContent());
                // 回复评论内容
                contentMap.put("replyComment", comment.getCommentContent());
                mailDTO.setContentMap(contentMap);
            }
            // 发送HTML邮件
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, EMAIL_HTML_KEY, mailDTO);
        } else {
            // 审核提醒
            String adminEmail = userMapper.selectOne(new LambdaQueryWrapper<User>()
                    .select(User::getEmail)
                    .eq(User::getId, BLOGGER_ID)).getEmail();
            mailDTO.setToEmail(adminEmail);
            mailDTO.setSubject(CHECK_REMIND);
            mailDTO.setContent("您收到一条新的回复，请前往后台管理页面审核.");
            // 发送普通邮件
            rabbitTemplate.convertAndSend(EMAIL_EXCHANGE, EMAIL_SIMPLE_KEY, mailDTO);
        }

    }

    @Override
    public List<ReplyVO> listReply(Integer commentId) {
        // 分页查询子评论
        List<ReplyVO> replyVOList = getBaseMapper().selectReplyByParentId(PageUtils.getLimit(), PageUtils.getSize(), commentId);
        // 子评论点赞Map
        Map<String, Integer> likeCountMap = redisService.getHashAll(COMMENT_LIKE_COUNT);
        replyVOList.forEach(item -> item.setLikeCount(likeCountMap.get(item.getId().toString())));
        return replyVOList;
    }

    @Override
    public PageResult<CommentBackVO> listCommentBackVO(ConditionDTO condition) {

        // 查询后台评论数量
        Long count = getBaseMapper().countComment(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台评论集合
        List<CommentBackVO> commentBackVOList = getBaseMapper().listCommentBackVO(PageUtils.getLimit(),
                PageUtils.getSize(), condition);
        return new PageResult<>(commentBackVOList, count);

    }

    @Override
    public void updateCommentCheck(CheckDTO check) {
        // 修改评论审核状态
        List<Comment> commentList = check.getIdList().stream().map(id -> Comment.builder().id(id).isCheck(check.getIsCheck()).build()).collect(Collectors.toList());
        this.updateBatchById(commentList);
    }
}

