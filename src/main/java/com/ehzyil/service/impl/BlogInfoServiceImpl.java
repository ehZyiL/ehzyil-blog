package com.ehzyil.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.ehzyil.domain.Article;
import com.ehzyil.domain.SiteConfig;
import com.ehzyil.mapper.*;
import com.ehzyil.model.vo.admin.ArticleRankVO;
import com.ehzyil.model.vo.admin.ArticleStatisticsVO;
import com.ehzyil.model.vo.admin.BlogBackInfoVO;
import com.ehzyil.model.vo.front.BlogInfoVO;
import com.ehzyil.model.vo.front.CategoryVO;
import com.ehzyil.model.vo.front.TagOptionVO;
import com.ehzyil.model.vo.front.UserViewVO;
import com.ehzyil.service.BlogInfoService;
import com.ehzyil.service.ISiteConfigService;
import com.ehzyil.service.RedisService;
import com.ehzyil.utils.IpUtils;
import com.ehzyil.utils.UserAgentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

import static com.ehzyil.constant.CommonConstant.FALSE;
import static com.ehzyil.constant.RedisConstant.*;
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
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private RedisService redisService;
    @Autowired
    private VisitLogMapper visitLogMapper;

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

    @Cacheable(key = "'blogInfo'", cacheManager = "caffeineCacheManager", cacheNames = "blogInfo")
    @Override
    public BlogInfoVO getBlogInfo() {
        //文章数量
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, PUBLIC.getStatus())
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

    @Cacheable(key = "'blogBackInfo'", cacheManager = "caffeineCacheManager", cacheNames = "blogInfo")
    @Override
    public BlogBackInfoVO getBlogBackInfo() {
        // 访问量
        Integer viewCount = redisService.getObject(BLOG_VIEW_COUNT);
        // 留言量
        Long messageCount = messageMapper.selectCount(null);
        // 用户量
        Long userCount = userMapper.selectCount(null);
        // 文章量
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .eq(Article::getIsDelete, FALSE));
        // 分类数据
        List<CategoryVO> categoryVOList = categoryMapper.selectCategoryVO();
        // 标签数据
        List<TagOptionVO> tagVOList = tagMapper.selectTagOptionList();
        // 查询用户浏览
        DateTime startTime = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime endTime = DateUtil.endOfDay(new Date());
        List<UserViewVO> userViewVOList = visitLogMapper.selectUserViewList(startTime, endTime);
        // 文章统计
        List<ArticleStatisticsVO> articleStatisticsList = articleMapper.selectArticleStatistics();
        // 查询redis访问量前五的文章
        Map<Object, Double> articleMap = redisService.zReverseRangeWithScore(ARTICLE_VIEW_COUNT, 0, 4);
        BlogBackInfoVO blogBackInfoVO = BlogBackInfoVO.builder()
                .articleStatisticsList(articleStatisticsList)
                .tagVOList(tagVOList)
                .viewCount(viewCount)
                .messageCount(messageCount)
                .userCount(userCount)
                .articleCount(articleCount)
                .categoryVOList(categoryVOList)
                .userViewVOList(userViewVOList)
                .build();
        if (CollectionUtils.isNotEmpty(articleMap)) {
            // 查询文章排行
            List<ArticleRankVO> articleRankVOList = listArticleRank(articleMap);
            blogBackInfoVO.setArticleRankVOList(articleRankVOList);
        }
        return blogBackInfoVO;

    }


    private List<ArticleRankVO> listArticleRank(Map<Object, Double> articleMap) {
        //提取文章id
        ArrayList<Integer> articleIdList = new ArrayList<>();
        articleMap.forEach((key, value) -> articleIdList.add((Integer) key));

        //查询文章
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>().
                select(Article::getId, Article::getArticleTitle)
                .in(Article::getId, articleIdList));
        //封装数据
        return articleList.stream().map(article ->
                        ArticleRankVO.builder()
                                .articleTitle(article.getArticleTitle())
                                //查询浏览量
                                .viewCount(articleMap.get(article.getId()).intValue())
                                .build()).
                //排序 反转
                        sorted(Comparator.comparingInt(ArticleRankVO::getViewCount).reversed())
                .collect(Collectors.toList());
    }
}
