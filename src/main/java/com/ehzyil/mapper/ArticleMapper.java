package com.ehzyil.mapper;

import com.ehzyil.domain.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-26
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 查询前台文章列表
     * @param limit
     * @param size
     * @return
     */
    List<ArticleHomeVO> selectArticleHomeList(@Param("limit") Long limit, @Param("size") Long size);

    /**
     * 查询前台推荐文章
     * @return
     */
    List<ArticleRecommendVO> selectArticleRecommend();

    /**
     * 根据文章id查询文章
     * @param articleId
     * @return
     */
    ArticleVO getArticleById(Integer articleId);

    /**
     * 查询上一篇文章
     * @param articleId
     * @return
     */
    ArticlePaginationVO selectLastArticle(Integer articleId);

    /**
     * 查询下一篇文章
     * @param articleId
     * @return
     */
    ArticlePaginationVO selectNextArticle(Integer articleId);

    /**
     * 查询符合条件的文章
     * @param limit
     * @param size
     * @param condition
     * @return
     */
    List<ArticleConditionVO> listArticleByCondition(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);
}
