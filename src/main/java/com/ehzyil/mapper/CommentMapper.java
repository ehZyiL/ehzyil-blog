package com.ehzyil.mapper;

import com.ehzyil.domain.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.CommentBackVO;
import com.ehzyil.model.vo.front.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface CommentMapper extends BaseMapper<Comment> {
    /**
     * 根据评论ids获取评论数
     * @param typeIdList
     * @param commentType
     * @return
     */
    List<CommentCountVO>  selectCommentCountByTypeId(@Param("typeIdList") List<Integer> typeIdList, @Param("commentType") String commentType);

    /**
     * 查询最近评论
     * @return
     */
    List<RecentCommentVO>  selectRecentCommentVOList();

    /**
     * 查询父评论
     * @param limit
     * @param size
     * @param conditionDTO
     * @return
     */
    List<CommentVO> selectParentComment(
            @Param("limit")Long limit,
            @Param("size")Long size,
            @Param("conditionDTO") ConditionDTO conditionDTO);

    /**
     * 查看回复评论
     * @param parentCommentIdList
     * @return
     */
    List<CommentReplyVO> selectReplyByParentIdList(@Param("parentCommentIdList") List<Integer> parentCommentIdList);

    /**
     * 父评论的回复数
     * @param typeIdList
     * @param commentType
     * @return
     */
    List<CommentReplyCountVO>  selectReplyCountByParentId(@Param("typeIdList") List<Integer> typeIdList, @Param("commentType") String commentType);
    /**
     * 查询父评论下的子评论
     *
     * @param limit     页码
     * @param size      大小
     * @param commentId 父评论id
     * @return 回复评论集合
     */
    List<ReplyVO> selectReplyByParentId(@Param("limit") Long limit, @Param("size") Long size, @Param("commentId") Integer commentId);

   /**
     * 查询后台评论列表
     *
     * @param limit     分页
     * @param size      大小
     * @param condition 条件
     * @return 评论集合
     */
    List<CommentBackVO> listCommentBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);

    /**
     * 查询评论数量
     *
     * @param condition 条件
     * @return 评论数量
     */
    Long countComment(@Param("condition") ConditionDTO condition);
}
