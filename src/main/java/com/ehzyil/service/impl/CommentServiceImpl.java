package com.ehzyil.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.Comment;
import com.ehzyil.domain.SiteConfig;
import com.ehzyil.mapper.CommentMapper;
import com.ehzyil.model.dto.CommentDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.*;
import com.ehzyil.service.ICommentService;
import com.ehzyil.service.RedisService;
import com.ehzyil.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
        commentReplyVOS.forEach(commentReplyVO ->  commentReplyVO.setLikeCount(Optional.ofNullable(likeCountMap.get(commentReplyVO.getId().toString())).orElse(0)));
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
}

