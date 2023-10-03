package com.ehzyil.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ehzyil.annotation.OptLogger;
import com.ehzyil.domain.SiteConfig;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.service.ISiteConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.ehzyil.enums.OptTypeConstant.UPDATE;
import static com.ehzyil.enums.OptTypeConstant.UPLOAD;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Api(tags = "网站配置模块")
@RestController
public class SiteConfigController {
    @Autowired
    private ISiteConfigService webConfigService;

    /**
     * 获取网站配置
     *
     * @return {@link Result<SiteConfig>} 网站配置
     */
    @ApiOperation(value = "获取网站配置")
    @SaCheckPermission("web:site:list")
    @GetMapping("/admin/site/list")
    public Result<SiteConfig> getSiteConfig() {
        return Result.success(webConfigService.getSiteConfig());
    }

    /**
     * 更新网站配置
     *
     * @param siteConfig 网站配置
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "更新网站配置")
    @SaCheckPermission("web:site:update")
    @PutMapping("/admin/site/update")
    public Result<?> updateSiteConfig(@RequestBody SiteConfig siteConfig) {
        webConfigService.updateSiteConfig(siteConfig);
        return Result.success();
    }
    /**
     * 上传网站配置图片
     *
     * @param file 图片
     * @return {@link Result<String>} 图片路径
     */
    @OptLogger(value = UPLOAD)
    @ApiOperation(value = "上传网站配置图片")
    @ApiImplicitParam(name = "file", value = "配置图片", required = true, dataType = "MultipartFile")
    @SaCheckPermission("web:site:upload")
    @PostMapping("/admin/site/upload")
    public Result<String> uploadSiteImg(@RequestParam("file") MultipartFile file) {
        return Result.success(webConfigService.uploadSiteImg(file));
    }

}
