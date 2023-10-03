package com.ehzyil.service;

import com.ehzyil.domain.SiteConfig;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface ISiteConfigService extends IService<SiteConfig> {
    /**
     * 获取网站配置
     * @return
     */
    SiteConfig getSiteConfig();


    /**
     * 更新网站配置
     *
     * @param siteConfig 网站配置
     */
    void updateSiteConfig(SiteConfig siteConfig);

    /**
     * 上传网站配置图片
     * @param file
     * @return
     */
    String uploadSiteImg(MultipartFile file);
}
