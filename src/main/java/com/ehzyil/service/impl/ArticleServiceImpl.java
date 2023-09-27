package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.Article;
import com.ehzyil.mapper.ArticleMapper;
import com.ehzyil.mapper.CategoryMapper;
import com.ehzyil.mapper.TagMapper;
import com.ehzyil.model.vo.*;
import com.ehzyil.service.IArticleService;
import com.ehzyil.service.ICategoryService;
import com.ehzyil.service.ITagService;
import com.ehzyil.utils.PageUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    private TagMapper tagMapper;

    @Autowired
    private ITagService tagService;

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

    @Override
    public List<ArticleRecommendVO> listArticleRecommendVO() {
        return articleMapper.selectArticleRecommend();
    }

    @Override
    public ArticleVO getArticleById(Integer articleId) {
        //查询文章
        ArticleVO articleVO = articleMapper.getArticleById(articleId);

        // 浏览量+1
        //TODO

        //查询上一篇文章
        ArticlePaginationVO lastArticle = articleMapper.selectLastArticle(articleId);
        articleVO.setLastArticle(lastArticle);

        //查询下一篇文章
        ArticlePaginationVO nextArticle = articleMapper.selectNextArticle(articleId);
        articleVO.setNextArticle(nextArticle);

        // 查询浏览量
        //TODO

        // 查询点赞量
        //TODO

        return articleVO;
    }

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
                    BeanUtils.copyProperties(article,archiveVO);
                    return archiveVO;
                })
                .collect(Collectors.toList());

        return new PageResult<>(archiveList,page.getSize());
    }
}
