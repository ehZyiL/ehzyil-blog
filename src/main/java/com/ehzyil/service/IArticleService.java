package com.ehzyil.service;

import com.ehzyil.domain.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.vo.front.*;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IArticleService extends IService<Article> {

    /**
     * 查询文章列表
     * @return
     */
    PageResult<ArticleHomeVO> listArticleHomeVO();

    /**
     * 查询前台推荐文章
     * @return
     */
    List<ArticleRecommendVO> listArticleRecommendVO();

    /**
     * 查看文章
     * @param articleId
     * @return
     */
    ArticleVO getArticleById(Integer articleId);

    /**
     * 查看文章归档
     *
     * @return
     */
    PageResult<ArchiveVO>  listArchiveVO(Long current, Long size);

}
