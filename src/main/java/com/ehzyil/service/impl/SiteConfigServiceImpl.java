package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.SiteConfig;
import com.ehzyil.mapper.SiteConfigMapper;
import com.ehzyil.service.ISiteConfigService;
import com.ehzyil.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.ehzyil.constant.RedisConstant.SITE_SETTING;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class SiteConfigServiceImpl extends ServiceImpl<SiteConfigMapper, SiteConfig> implements ISiteConfigService {

    @Autowired
    private RedisService redisService;

    @Override
    public SiteConfig getSiteConfig() {
        SiteConfig siteConfig = redisService.getObject(SITE_SETTING);
        if (Objects.isNull(siteConfig)) {
            //从数据库获取
            siteConfig = getBaseMapper().selectById(1);
            redisService.setObject(SITE_SETTING, siteConfig);
        }
        return siteConfig;
    }
}
