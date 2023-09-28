package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ehzyil.domain.Article;
import com.ehzyil.domain.SiteConfig;
import com.ehzyil.mapper.ArticleMapper;
import com.ehzyil.mapper.CategoryMapper;
import com.ehzyil.mapper.TagMapper;
import com.ehzyil.model.vo.BlogInfoVO;
import com.ehzyil.service.BlogInfoService;
import com.ehzyil.service.ISiteConfigService;
import com.ehzyil.service.RedisService;
import com.ehzyil.utils.IpUtils;
import com.ehzyil.utils.UserAgentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

import static com.ehzyil.constant.CommonConstant.FALSE;
import static com.ehzyil.constant.RedisConstant.BLOG_VIEW_COUNT;
import static com.ehzyil.constant.RedisConstant.UNIQUE_VISITOR;
import static com.ehzyil.enums.ArticleStatusEnum.PUBLIC;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/28-13:44
 */
@Service
public class BlogInfoServiceImpl implements BlogInfoService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ISiteConfigService siteConfigService;
    @Autowired
    private HttpServletRequest request;

    @Override
    public void report() {
        //获取用户IP
        String ipAddress = IpUtils.getIpAddress(request);
        Map<String, String> userAgentMap = UserAgentUtils.parseOsAndBrowser(request.getHeader("User-Agent"));
        //获取访问设备
        String browser = userAgentMap.get("browser");
        String os = userAgentMap.get("os");
        //生成唯一用户标识
        String flag = ipAddress + browser + os;
        String md5 = DigestUtils.md5DigestAsHex(flag.getBytes());
        Boolean aBoolean = redisService.hasSetValue(UNIQUE_VISITOR, md5);
        //判断是否访问
        if (!redisService.hasSetValue(UNIQUE_VISITOR, md5)) {
            //访问量+1
            redisService.incr(BLOG_VIEW_COUNT, 1);
            //保存唯一标识
            redisService.setSet(UNIQUE_VISITOR, md5);
        }
    }

    @Override
    public BlogInfoVO getBlogInfo() {
        //文章数量
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC)
                .eq(Article::getIsDelete, FALSE));
        // 分类数量
        Long categoryCount = categoryMapper.selectCount(null);
        // 标签数量
        Long tagCount = tagMapper.selectCount(null);
        // 博客访问量
        Integer count = redisService.getObject(BLOG_VIEW_COUNT);
        String viewCount = Optional.ofNullable(count).orElse(0).toString();
        //网站配置
        SiteConfig siteConfig = siteConfigService.getSiteConfig();


        return BlogInfoVO.builder()
                .articleCount(articleCount)
                .categoryCount(categoryCount)
                .tagCount(tagCount)
                .viewCount(viewCount)
                .siteConfig(siteConfig)
                .build();
    }
}
