package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.Comment;
import com.ehzyil.mapper.CommentMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.*;
import com.ehzyil.service.ICommentService;
import com.ehzyil.utils.PageUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .eq(Comment::getIsCheck, "1")
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
        //TODO  评论点赞

        //获取父评论ids
        List<Integer> parentCommentIdList = commentVOList.stream().map(CommentVO::getId).collect(Collectors.toList());
        // 分组查询每组父评论下的子评论前三条
        List<CommentReplyVO> commentReplyVOS = getBaseMapper().selectReplyByParentIdList(parentCommentIdList);
        // 封装子评论点赞量
        //TODO 子评论点赞

        // 根据父评论id生成对应子评论的Map
        Map<Integer, List<CommentReplyVO>> replyMap = commentReplyVOS.stream().collect(Collectors.groupingBy(CommentReplyVO::getParentId));
        // 父评论的回复数量
        List<CommentReplyCountVO> commentReplyCountVOS = getBaseMapper().selectReplyByParentId(parentCommentIdList, String.valueOf(conditionDTO.getCommentType()));
        //转为map
        Map<Integer, Integer> commentReplyCountVOMap = commentReplyCountVOS.stream().collect(Collectors.toMap(CommentReplyCountVO::getCommentId, CommentReplyCountVO::getReplyCount));

        // 封装评论数据
        commentVOList.forEach(item -> {
            //TODO 评论点赞
            item.setLikeCount(0);
            item.setReplyVOList(replyMap.get(item.getId()));
            item.setReplyCount(Optional.ofNullable(commentReplyCountVOMap.get(item.getId())).orElse(0));
        });
        return new PageResult<>(commentVOList, count);
    }
}
