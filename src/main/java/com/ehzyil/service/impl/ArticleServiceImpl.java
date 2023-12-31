package com.ehzyil.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.*;
import com.ehzyil.mapper.ArticleMapper;
import com.ehzyil.mapper.ArticleTagMapper;
import com.ehzyil.mapper.CategoryMapper;
import com.ehzyil.mapper.TagMapper;
import com.ehzyil.model.dto.*;
import com.ehzyil.model.vo.admin.ArticleBackVO;
import com.ehzyil.model.vo.admin.ArticleInfoVO;
import com.ehzyil.model.vo.admin.ArticleSearchVO;
import com.ehzyil.model.vo.front.*;
import com.ehzyil.service.IArticleService;
import com.ehzyil.service.ICategoryService;
import com.ehzyil.service.ITagService;
import com.ehzyil.service.RedisService;
import com.ehzyil.strategy.context.UploadStrategyContext;
import com.ehzyil.utils.BeanCopyUtils;
import com.ehzyil.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ehzyil.constant.CommonConstant.FALSE;
import static com.ehzyil.constant.RedisConstant.*;
import static com.ehzyil.enums.FilePathEnum.ARTICLE;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ITagService tagService;

    @Autowired
    private RedisService redisService;
    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Cacheable(key = "'listArticleHomeVO' ", cacheManager = "caffeineCacheManager", cacheNames = "listArticleHomeVO")
    @Override
    public PageResult<ArticleHomeVO> listArticleHomeVO() {

        //查询文章信息
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();

        articleLambdaQueryWrapper.eq(Article::getIsDelete, "0")
                .eq(Article::getStatus, "1");
        Long count = articleMapper.selectCount(articleLambdaQueryWrapper);

        if (count == 0) {
            return new PageResult<>();
        }
        List<ArticleHomeVO> articleHomeVOS = articleMapper.selectArticleHomeList(PageUtils.getLimit(), PageUtils.getSize());

        return new PageResult<>(articleHomeVOS, count);
    }

    @Cacheable(key = "'listArticleRecommendVO'", cacheManager = "caffeineCacheManager", cacheNames = "listArticleRecommendVO")
    @Override
    public List<ArticleRecommendVO> listArticleRecommendVO() {
        return articleMapper.selectArticleRecommend();
    }

    @Override
    public ArticleVO getArticleById(Integer articleId) {
        //查询文章
        ArticleVO articleVO = articleMapper.getArticleById(articleId);

        // 浏览量+1
        redisService.incrZet(ARTICLE_VIEW_COUNT, articleId, 1D);

        //查询上一篇文章
        ArticlePaginationVO lastArticle = articleMapper.selectLastArticle(articleId);
        articleVO.setLastArticle(lastArticle);

        //查询下一篇文章
        ArticlePaginationVO nextArticle = articleMapper.selectNextArticle(articleId);
        articleVO.setNextArticle(nextArticle);

        // 查询浏览量
        Double viewCount = Optional.ofNullable(redisService.getZsetScore(ARTICLE_VIEW_COUNT, articleId))
                .orElse((double) 0);

        articleVO.setViewCount(viewCount.intValue());
        // 查询点赞量
        Integer likeCount = redisService.getHash(ARTICLE_LIKE_COUNT, articleId.toString());
        articleVO.setLikeCount(Optional.ofNullable(likeCount).orElse(0));

        return articleVO;
    }

    @Cacheable(key = "'listArchiveVO_' + #current", cacheManager = "caffeineCacheManager", cacheNames = "listArchiveVO")
    @Override
    public PageResult<ArchiveVO> listArchiveVO(Long current, Long size) {
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getStatus, "1")
                .eq(Article::getIsDelete, "0")
                .orderByDesc(Article::getCreateTime);

        Page<Article> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        page(page, articleLambdaQueryWrapper);

        List<ArchiveVO> archiveList = page.getRecords()
                .stream()
                .map(article -> {
                    ArchiveVO archiveVO = new ArchiveVO();
                    BeanUtils.copyProperties(article, archiveVO);
                    return archiveVO;
                })
                .collect(Collectors.toList());

        return new PageResult<>(archiveList, page.getSize());
    }

    @Override
    public String saveArticleImages(MultipartFile file) {
        return uploadStrategyContext.executeUploadStrategy(file, ARTICLE.getFilePath());
    }

    @Override
    public PageResult<ArticleBackVO> listArticleBackVO(ConditionDTO condition) {

        // 查询文章数量
        Long count = articleMapper.countArticleBackVO(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询文章后台信息
        List<ArticleBackVO> articleBackVOList = articleMapper.selectArticleBackVO(PageUtils.getLimit(), PageUtils.getSize(), condition);
        // 浏览量
        Map<Object, Double> viewCountMap = redisService.getZsetAllScore(ARTICLE_VIEW_COUNT);
        // 点赞量
        Map<String, Integer> likeCountMap = redisService.getHashAll(ARTICLE_LIKE_COUNT);
        // 封装文章后台信息
        articleBackVOList.forEach(item -> {
            Double viewCount = Optional.ofNullable(viewCountMap.get(item.getId())).orElse((double) 0);
            item.setViewCount(viewCount.intValue());
            Integer likeCount = likeCountMap.get(item.getId().toString());
            item.setLikeCount(Optional.ofNullable(likeCount).orElse(0));
        });
        return new PageResult<>(articleBackVOList, count);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addArticle(ArticleDTO article) {
        // 保存文章分类
        Integer categoryId = saveArticleCategory(article);
        // 添加文章
        Article newArticle = BeanCopyUtils.copyBean(article, Article.class);
        if (StringUtils.isBlank(newArticle.getArticleCover())) {
            SiteConfig siteConfig = redisService.getObject(SITE_SETTING);
            newArticle.setArticleCover(siteConfig.getArticleCover());
        }
        newArticle.setCategoryId(categoryId);
        newArticle.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.insert(newArticle);
        // 保存文章标签
        saveArticleTag(article, newArticle.getId());
    }
    /**
     * 保存文章分类
     *
     * @param article 文章信息
     * @return 文章分类
     */
    private Integer saveArticleCategory(ArticleDTO article) {
        // 查询分类
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, article.getCategoryName()));
        // 分类不存在
        if (Objects.isNull(category)) {
            category = Category.builder()
                    .categoryName(article.getCategoryName())
                    .build();
            // 保存分类
            categoryMapper.insert(category);
        }
        return category.getId();
    }
    /**
     * 保存文章标签
     *
     * @param article   文章信息
     * @param articleId 文章id
     */
    private void saveArticleTag(ArticleDTO article, Integer articleId) {
        // 删除文章标签
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .eq(ArticleTag::getArticleId, articleId));
        // 标签名列表
        List<String> tagNameList = article.getTagNameList();
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            // 查询出已存在的标签
            List<Tag> existTagList = tagMapper.selectTagList(tagNameList);
            List<String> existTagNameList = existTagList.stream()
                    .map(Tag::getTagName)
                    .collect(Collectors.toList());
            List<Integer> existTagIdList = existTagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            // 移除已存在的标签列表
            tagNameList.removeAll(existTagNameList);
            // 含有新标签
            if (CollectionUtils.isNotEmpty(tagNameList)) {
                // 新标签列表
                List<Tag> newTagList = tagNameList.stream()
                        .map(item -> Tag.builder()
                                .tagName(item)
                                .build())
                        .collect(Collectors.toList());
                // 批量保存新标签
                tagService.saveBatch(newTagList);
                // 获取新标签id列表
                List<Integer> newTagIdList = newTagList.stream()
                        .map(Tag::getId)
                        .collect(Collectors.toList());
                // 新标签id添加到id列表
                existTagIdList.addAll(newTagIdList);
            }
            // 将所有的标签绑定到文章标签关联表
            articleTagMapper.saveBatchArticleTag(articleId, existTagIdList);
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteArticle(List<Integer> articleIdList) {
        // 删除文章标签
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        // 删除文章
        articleMapper.deleteBatchIds(articleIdList);
    }

    @Override
    public void updateArticleDelete(DeleteDTO delete) {
        // 批量更新文章删除状态
        List<Article> articleList = delete.getIdList()
                .stream()
                .map(id -> Article.builder()
                        .id(id)
                        .isDelete(delete.getIsDelete())
                        .isTop(FALSE)
                        .isRecommend(FALSE)
                        .build())
                .collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateArticle(ArticleDTO article) {
        // 保存文章分类
        Integer categoryId = saveArticleCategory(article);
        // 修改文章
        Article newArticle = BeanCopyUtils.copyBean(article, Article.class);
        newArticle.setCategoryId(categoryId);
        newArticle.setUserId(StpUtil.getLoginIdAsInt());
        baseMapper.updateById(newArticle);
        // 保存文章标签
        saveArticleTag(article, newArticle.getId());
    }

    @Override
    public ArticleInfoVO editArticle(Integer articleId) {
        // 查询文章信息
        ArticleInfoVO articleInfoVO = articleMapper.selectArticleInfoById(articleId);
        Assert.notNull(articleInfoVO, "没有该文章");
        // 查询文章分类名称
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getCategoryName)
                .eq(Category::getId, articleInfoVO.getCategoryId()));
        // 查询文章标签名称
        List<String> tagNameList = tagMapper.selectTagNameByArticleId(articleId);
        articleInfoVO.setCategoryName(category.getCategoryName());
        articleInfoVO.setTagNameList(tagNameList);
        return articleInfoVO;
    }

    @Override
    public void updateArticleTop(TopDTO top) {
        // 修改文章置顶状态
        Article newArticle = Article.builder()
                .id(top.getId())
                .isTop(top.getIsTop())
                .build();
        articleMapper.updateById(newArticle);
    }

    @Override
    public void updateArticleRecommend(RecommendDTO recommend) {
        // 修改文章推荐状态
        Article newArticle = Article.builder()
                .id(recommend.getId())
                .isRecommend(recommend.getIsRecommend())
                .build();
        articleMapper.updateById(newArticle);
    }

    @Override
    public List<ArticleSearchVO> listArticlesBySearch(String keyword) {
        return null;
    }
}
