package com.ehzyil.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ehzyil.annotation.AccessLimit;
import com.ehzyil.annotation.OptLogger;
import com.ehzyil.annotation.VisitLogger;
import com.ehzyil.enums.LikeTypeEnum;
import com.ehzyil.model.dto.*;
import com.ehzyil.model.vo.admin.ArticleBackVO;
import com.ehzyil.model.vo.admin.ArticleInfoVO;
import com.ehzyil.model.vo.admin.ArticleSearchVO;
import com.ehzyil.model.vo.front.*;
import com.ehzyil.service.IArticleService;
import com.ehzyil.strategy.context.LikeStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ehzyil.enums.OptTypeConstant.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-26
 */
@Api(tags = "文章模块")
@RestController
public class ArticleController {

    @Autowired
    private IArticleService articleService;

    @Autowired
    private LikeStrategyContext likeStrategyContext;
    /**
     * 查看首页文章列表
     *
     * @return {@link Result< ArticleHomeVO >}
     */
    @VisitLogger(value = "首页")
    @ApiOperation(value = "查看首页文章列表")
    @GetMapping("/article/list")
    public Result<PageResult<ArticleHomeVO>> listArticleHomeVO() {
        return Result.success(articleService.listArticleHomeVO());
    }

    @ApiOperation(value = "查看推荐文章")
    @GetMapping("/article/recommend")
    public Result<List<ArticleRecommendVO>> listArticleRecommendVO() {
        return Result.success(articleService.listArticleRecommendVO());
    }
    @VisitLogger(value = "文章")
    @ApiOperation(value = "根据id查看文章")
    @GetMapping("/article/{articleId}")
    public Result<ArticleVO> getArticleById(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.getArticleById(articleId));
    }
    @VisitLogger(value = "归档")
    @ApiOperation(value = "查看文章归档")
    @GetMapping("/archives/list")
    public Result<PageResult<ArchiveVO>> listArchiveVO(@RequestParam("current") Long current, @RequestParam("size") Long size) {
        return Result.success(articleService.listArchiveVO(current,size));
    }

    @SaCheckLogin
    @ApiOperation(value = "点赞文章")
    @AccessLimit(seconds = 10, maxCount = 3)
    @SaCheckPermission("blog:article:like")
    @PostMapping("/article/{articleId}/like")
    public Result<?> likeArticle(@PathVariable("articleId") Integer articleId) {
        likeStrategyContext.executeLikeStrategy(LikeTypeEnum.ARTICLE, articleId);
        return Result.success();
    }


    /**
     * 上传文章图片
     *
     * @param file 文件
     * @return {@link Result<String>} 文章图片地址
     */
    @OptLogger(value = UPLOAD)
    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("blog:article:upload")
    @PostMapping("/admin/article/upload")
    public Result<String> saveArticleImages(@RequestParam("file") MultipartFile file) {
        return Result.success(articleService.saveArticleImages(file));
    }

    /**
     * 查看后台文章列表
     *
     * @param condition 条件
     * @return {@link Result<ArticleBackVO>} 后台文章列表
     */
    @ApiOperation(value = "查看后台文章列表")
    @SaCheckPermission("blog:article:list")
    @GetMapping("/admin/article/list")
    public Result<PageResult<ArticleBackVO>> listArticleBackVO(ConditionDTO condition) {
        return Result.success(articleService.listArticleBackVO(condition));
    }


    /**
     * 添加文章
     *
     * @param article 文章信息
     * @return {@link Result<>}
     */
    @OptLogger(value = ADD)
    @ApiOperation(value = "添加文章")
    @SaCheckPermission("blog:article:add")
    @PostMapping("/admin/article/add")
    public Result<?> addArticle(@Validated @RequestBody ArticleDTO article) {
        articleService.addArticle(article);
        return Result.success();
    }

    /**
     * 删除文章
     *
     * @param articleIdList 文章id集合
     * @return {@link Result<>}
     */
    @OptLogger(value = DELETE)
    @ApiOperation(value = "删除文章")
    @SaCheckPermission("blog:article:delete")
    @DeleteMapping("/admin/article/delete")
    public Result<?> deleteArticle(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticle(articleIdList);
        return Result.success();
    }

    /**
     * 回收或恢复文章
     *
     * @param delete 逻辑删除
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "回收或恢复文章")
    @SaCheckPermission("blog:article:recycle")
    @PutMapping("/admin/article/recycle")
    public Result<?> updateArticleDelete(@Validated @RequestBody DeleteDTO delete) {
        articleService.updateArticleDelete(delete);
        return Result.success();
    }

    /**
     * 修改文章
     *
     * @param article 文章信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改文章")
    @SaCheckPermission("blog:article:update")
    @PutMapping("/admin/article/update")
    public Result<?> updateArticle(@Validated @RequestBody ArticleDTO article) {
        articleService.updateArticle(article);
        return Result.success();
    }

    /**
     * 编辑文章
     * //TODO
     * @param articleId 文章id
     * @return {@link Result<ArticleInfoVO>} 后台文章
     */
    @ApiOperation(value = "编辑文章")
    @SaCheckPermission("blog:article:edit")
    @GetMapping("/admin/article/edit/{articleId}")
    public Result<ArticleInfoVO> editArticle(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.editArticle(articleId));
    }
    /**
     * 置顶文章
     *
     * @param top 置顶信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "置顶文章")
    @SaCheckPermission("blog:article:top")
    @PutMapping("/admin/article/top")
    public Result<?> updateArticleTop(@Validated @RequestBody TopDTO top) {
        articleService.updateArticleTop(top);
        return Result.success();
    }

    /**
     * 推荐文章
     *
     * @param recommend 推荐信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "推荐文章")
    @SaCheckPermission("blog:article:recommend")
    @PutMapping("/admin/article/recommend")
    public Result<?> updateArticleRecommend(@Validated @RequestBody RecommendDTO recommend) {
        articleService.updateArticleRecommend(recommend);
        return Result.success();
    }
    /**
     * 搜索文章
     *
     * @param keyword 关键字
     * @return {@link Result<ArticleSearchVO>} 文章列表
     */
    @ApiOperation(value = "搜索文章")
    @GetMapping("/article/search")
    public Result<List<ArticleSearchVO>> listArticlesBySearch(String keyword) {
        return Result.success(articleService.listArticlesBySearch(keyword));
    }
}
