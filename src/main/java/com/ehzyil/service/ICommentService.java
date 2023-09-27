package com.ehzyil.service;

import com.ehzyil.domain.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.CommentVO;
import com.ehzyil.model.vo.PageResult;
import com.ehzyil.model.vo.RecentCommentVO;

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
}
