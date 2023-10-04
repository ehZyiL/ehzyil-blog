package com.ehzyil.service;

import com.ehzyil.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.CheckDTO;
import com.ehzyil.model.dto.CommentDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.CommentBackVO;
import com.ehzyil.model.vo.front.CommentVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.model.vo.front.RecentCommentVO;
import com.ehzyil.model.vo.front.ReplyVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface ICommentService extends IService<Comment> {

    /**
     * 查看最新评论
     * @return
     */
    List<RecentCommentVO> listRecentCommentVO();

    /**
     * 查看评论
     * @param conditionDTO
     * @return
     */
    PageResult<CommentVO> listCommentVO(ConditionDTO conditionDTO);

    /**
     * 评论
     * @param commentDTO
     * @return
     */
    void addComment(CommentDTO commentDTO);

    /**
     * 查看回复评论
     * @param commentId
     * @return
     */
    List<ReplyVO> listReply(Integer commentId);

    /**
     * 查看后台评论列表
     *
     * @param condition 条件
     * @return 后台评论列表
     */
    PageResult<CommentBackVO> listCommentBackVO(ConditionDTO condition);


    /**
     * 审核评论
     *
     * @param check 审核信息
     */
    void updateCommentCheck(CheckDTO check);
}
