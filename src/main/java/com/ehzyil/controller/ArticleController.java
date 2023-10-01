package com.ehzyil.controller;


import com.ehzyil.annotation.OptLogger;
import com.ehzyil.annotation.VisitLogger;
import com.ehzyil.model.vo.*;
import com.ehzyil.service.IArticleService;
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
}
