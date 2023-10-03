package com.ehzyil.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ehzyil.annotation.AccessLimit;
import com.ehzyil.annotation.OptLogger;
import com.ehzyil.annotation.VisitLogger;
import com.ehzyil.enums.LikeTypeEnum;
import com.ehzyil.model.vo.*;
import com.ehzyil.service.IArticleService;
import com.ehzyil.strategy.context.LikeStrategyContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * @return {@link Result<ArticleHomeVO>}
     */
    @VisitLogger(value = "首页")
    @ApiOperation(value = "查看首页文章列表")
    @GetMapping("/article/list")
    public Result<PageResult<ArticleHomeVO>> listArticleHomeVO() {
        return Result.success(articleService.listArticleHomeVO());
    }
    @SaCheckPermission("blog:article:recommend")
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
}
