package com.ehzyil.controller;

import com.ehzyil.model.vo.admin.BlogBackInfoVO;
import com.ehzyil.model.vo.front.BlogInfoVO;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.service.BlogInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 博客控制器
 *
 * @author ican
 **/
@Api(tags = "博客模块")
@RestController
public class BlogInfoController {

    @Autowired
    private BlogInfoService blogInfoService;

    /**
     * 上传访客信息
     *
     * @return {@link Result<>}
     */
    @ApiOperation(value = "上传访客信息")
    @PostMapping("/report")
    public Result<?> report() {
        blogInfoService.report();
        return Result.success();
    }

    /**
     * 查看博客信息
     *
     * @return {@link Result<BlogInfoVO>} 博客信息
     */
    @ApiOperation(value = "查看博客信息")
    @GetMapping("/")
    public Result<BlogInfoVO> getBlogInfo() {
        return Result.success(blogInfoService.getBlogInfo());
    }

    /**
     * 查看后台信息
     *
     * @return {@link Result<BlogBackInfoVO>} 后台信息
     */
    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    public Result<BlogBackInfoVO> getBlogBackInfo() {
        return Result.success(blogInfoService.getBlogBackInfo());
    }

}