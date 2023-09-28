package com.ehzyil.service;

import com.ehzyil.domain.SiteConfig;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
