package com.ehzyil.mapper;

import com.ehzyil.domain.ArticleTag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    void saveBatchArticleTag(@Param("articleId") Integer articleId, @Param("existTagIdList") List<Integer> existTagIdList);
}
