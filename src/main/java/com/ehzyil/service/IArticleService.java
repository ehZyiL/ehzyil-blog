package com.ehzyil.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.domain.Article;
import com.ehzyil.model.dto.*;
import com.ehzyil.model.vo.admin.ArticleBackVO;
import com.ehzyil.model.vo.admin.ArticleInfoVO;
import com.ehzyil.model.vo.admin.ArticleSearchVO;
import com.ehzyil.model.vo.front.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IArticleService extends IService<Article> {

    /**
     * 查询文章列表
     *
     * @return
     */
    PageResult<ArticleHomeVO> listArticleHomeVO();

    /**
     * 查询前台推荐文章
     *
     * @return
     */
    List<ArticleRecommendVO> listArticleRecommendVO();

    /**
     * 查看文章
     *
     * @param articleId
     * @return
     */
    ArticleVO getArticleById(Integer articleId);

    /**
     * 查看文章归档
     *
     * @return
     */
    PageResult<ArchiveVO> listArchiveVO(Long current, Long size);

    String saveArticleImages(MultipartFile file);

    /**
     * 查看后台文章列表
     *
     * @param condition 条件
     * @return 后台文章列表
     */
    PageResult<ArticleBackVO> listArticleBackVO(ConditionDTO condition);


    /**
     * 添加文章
     *
     * @param article 文章
     */
    void addArticle(ArticleDTO article);

    /**
     * 删除文章
     *
     * @param articleIdList 文章id
     */
    void deleteArticle(List<Integer> articleIdList);

    /**
     * 回收或恢复文章
     *
     * @param delete 逻辑删除
     */
    void updateArticleDelete(DeleteDTO delete);

    /**
     * 修改文章
     *
     * @param article 文章
     */
    void updateArticle(ArticleDTO article);

    /**
     * 编辑文章
     *
     * @param articleId 文章id
     * @return 文章
     */
    ArticleInfoVO editArticle(Integer articleId);


    /**
     * 修改文章置顶状态
     *
     * @param top 置顶
     */
    void updateArticleTop(TopDTO top);

    /**
     * 修改文章推荐状态
     *
     * @param recommend 推荐
     */
    void updateArticleRecommend(RecommendDTO recommend);

    List<ArticleSearchVO> listArticlesBySearch(String keyword);
}
