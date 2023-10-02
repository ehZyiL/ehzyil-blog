package com.ehzyil.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.Comment;
import com.ehzyil.domain.Talk;
import com.ehzyil.domain.User;
import com.ehzyil.mapper.CommentMapper;
import com.ehzyil.mapper.TalkMapper;
import com.ehzyil.model.vo.CommentCountVO;
import com.ehzyil.model.vo.PageResult;
import com.ehzyil.model.vo.TalkVO;
import com.ehzyil.service.ITalkService;
import com.ehzyil.service.IUserService;
import com.ehzyil.service.RedisService;
import com.ehzyil.utils.HTMLUtils;
import com.ehzyil.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.ehzyil.constant.RedisConstant.TALK_LIKE_COUNT;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class TalkServiceImpl extends ServiceImpl<TalkMapper, Talk> implements ITalkService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisService redisService;

    @Override
    public List<String> listTalkHome() {
        //查询最新
        List<Talk> talkList = getBaseMapper().selectList(new LambdaQueryWrapper<Talk>()
                .select(Talk::getTalkContent)
                .orderByDesc(Talk::getIsTop)
                .orderByDesc(Talk::getCreateTime)
                .orderByDesc(Talk::getId)
                .last("limit 5")
        );
        return talkList.stream()
                .map(item ->
                        item.getTalkContent().length()
                                > 200 ?
                                HTMLUtils.deleteHtmlTag(item.getTalkContent().substring(0, 200))
                                :
                                HTMLUtils.deleteHtmlTag(item.getTalkContent())).collect(Collectors.toList());


    }

    @Override
    public PageResult<TalkVO> listTalkVO(Long current, Long size) {
//        //查询说说
//        LambdaQueryWrapper<Talk> talkLambdaQueryWrapper = new LambdaQueryWrapper<>();
//        talkLambdaQueryWrapper
//                .eq(Talk::getStatus, "1")
//                .orderByDesc(Talk::getIsTop)
//                .orderByDesc(Talk::getId);
//
//
//        //分页查询说说
//        Page<Talk> page = new Page<>();
//        page.setCurrent(current);
//        page.setSize(size);
//        page(page, talkLambdaQueryWrapper);
//
//        List<Talk> talkList = page.getRecords();
//
//        //获取说说id列表
//        List<Integer> userIdList = talkList.stream().map(Talk::getUserId).collect(Collectors.toList());
//        // 查询用户头像 昵称
//        LambdaQueryWrapper<User> userLambdaQueryWrapper=new LambdaQueryWrapper<User>();
//        userLambdaQueryWrapper.select(User::getId,User::getAvatar,User::getNickname).in(User::getId,userIdList);
//        List<User> userList = userService.getBaseMapper().selectList(userLambdaQueryWrapper);

        // 查询说说总量
        Long count = getBaseMapper().selectCount((new LambdaQueryWrapper<Talk>()
                .eq(Talk::getStatus, "1")));

        if (count == 0) {
            return new PageResult<>();
        }

        List<TalkVO> talkVOS = getBaseMapper().selectTalkList(PageUtils.getLimit(), PageUtils.getSize());

        //获取说说id列表
        List<Integer> typeIdList = talkVOS.stream().map(TalkVO::getId).collect(Collectors.toList());

        // 查询说说评论量
        List<CommentCountVO> commentCountVOS = commentMapper.selectCommentCountByTypeId(typeIdList, "3");
        //转换为Map key为talkId value为评论数
        Map<Integer, Integer> commentCountMap = commentCountVOS.stream().collect(Collectors.toMap(CommentCountVO::getCommentCount, CommentCountVO::getCommentCount));

        // 查询说说点赞量
        Map<String, Integer> likeCountMap = redisService.getHashAll(TALK_LIKE_COUNT);

        List<TalkVO> talkVOList = new ArrayList<>();
        // 封装说说
        talkVOS.forEach(talk -> {
            TalkVO talkVO = new TalkVO();
            BeanUtils.copyProperties(talk, talkVO);

            talkVO.setCommentCount(Optional.ofNullable(commentCountMap.get(talkVO.getId())).orElse(0));
            talkVO.setLikeCount(Optional.ofNullable(likeCountMap.get(talk.getId().toString())).orElse(0));

            // 转换图片格式
            if (Objects.nonNull(talk.getImages())) {
                List<String> list = JSON.parseObject(talk.getImages(), List.class);
                talkVO.setImgList(list);
            }
            talkVOList.add(talkVO);
        });

        return new PageResult<>(talkVOList, count);
    }

    @Override
    public TalkVO getTalkById(Long talkId) {
        Talk talk = getBaseMapper().selectById(talkId);
        TalkVO talkVO = new TalkVO();
        BeanUtils.copyProperties(talk, talkVO);
        // 查询用户头像 昵称
        User user = userService.getBaseMapper().selectOne(
                new LambdaQueryWrapper<User>().
                        select(User::getId, User::getAvatar, User::getNickname)
                        .eq(User::getId, talk.getUserId()));
        talkVO.setAvatar(Optional.ofNullable(user.getAvatar()).orElse(""));
        talkVO.setNickname(Optional.ofNullable(user.getNickname()).orElse(""));

        // 查询说说评论量
        long count = commentMapper.selectCount(new LambdaQueryWrapper<Comment>()
                .eq(Comment::getTypeId, talk.getId())
                .eq(Comment::getCommentType, "3"));
        talkVO.setCommentCount(Math.toIntExact(Optional.ofNullable(count).orElse(0L)));

        // 查询说说点赞量
        Integer likeCount = redisService.getHash(TALK_LIKE_COUNT, talkId.toString());
        talkVO.setLikeCount(Optional.ofNullable(likeCount).orElse(0));

        // 转换图片格式
        if (Objects.nonNull(talk.getImages())) {
            List<String> list = JSON.parseObject(talk.getImages(), List.class);
            talkVO.setImgList(list);
        }

        return talkVO;
    }
}
