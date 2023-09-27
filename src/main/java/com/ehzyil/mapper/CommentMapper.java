package com.ehzyil.mapper;

import com.ehzyil.domain.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.vo.CommentCountVO;
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

    List<CommentCountVO>  selectCommentCountByTypeId(@Param("typeIdList") List<Integer> typeIdList, @Param("commentType") String commentType);
}
