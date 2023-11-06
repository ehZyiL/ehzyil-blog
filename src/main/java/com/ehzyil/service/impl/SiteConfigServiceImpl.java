package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.SiteConfig;
import com.ehzyil.mapper.BlogFileMapper;
import com.ehzyil.mapper.SiteConfigMapper;
import com.ehzyil.service.ISiteConfigService;
import com.ehzyil.service.RedisService;
import com.ehzyil.strategy.context.UploadStrategyContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.ehzyil.constant.RedisConstant.SITE_SETTING;
import static com.ehzyil.enums.FilePathEnum.CONFIG;

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
    @Autowired
    private BlogFileMapper blogFileMapper;

    @Autowired
    private UploadStrategyContext uploadStrategyContext;

//    @Cacheable(key = "SiteConfig", cacheManager = "caffeineCacheManager", cacheNames = "SiteConfig")
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

//    @CacheEvict(key = "SiteConfig", cacheManager = "caffeineCacheManager", cacheNames = "SiteConfig")
    @Override
    public void updateSiteConfig(SiteConfig siteConfig) {
        baseMapper.updateById(siteConfig);
        redisService.deleteObject(SITE_SETTING);
    }

    @Override
    public String uploadSiteImg(MultipartFile file) {
        // 上传文件
        return uploadStrategyContext.executeUploadStrategy(file, CONFIG.getPath());
    }
}
