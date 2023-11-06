package com.ehzyil.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.Article;
import com.ehzyil.domain.Category;
import com.ehzyil.mapper.ArticleMapper;
import com.ehzyil.mapper.CategoryMapper;
import com.ehzyil.model.dto.CategoryDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.CategoryBackVO;
import com.ehzyil.model.vo.front.*;
import com.ehzyil.service.ICategoryService;
import com.ehzyil.utils.BeanCopyUtils;
import com.ehzyil.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Cacheable(key = "'listCategory'", cacheManager = "caffeineCacheManager", cacheNames = "listCategory")
    @Override
    public List<CategoryVO> listCategoryVO() {
        return categoryMapper.selectCategoryVO();
    }

    @Override
    public ArticleConditionList listArticleCategory(ConditionDTO condition) {

        //查询符合条件的文章
        List<ArticleConditionVO> articleConditionVOS = articleMapper.listArticleByCondition(
                PageUtils.getLimit(), PageUtils.getSize(), condition);
        //查询分类名称
        String categoryName = getBaseMapper().selectOne(
                new LambdaQueryWrapper<Category>()
                        .select(Category::getCategoryName)
                        .eq(Category::getId, condition.getCategoryId())
        ).getCategoryName();

        return ArticleConditionList.builder().articleConditionVOList(articleConditionVOS).name(categoryName).build();
    }

    @Cacheable(key = "'listCategoryBack'", cacheManager = "caffeineCacheManager", cacheNames = "listCategoryBack")
    @Override
    public PageResult<CategoryBackVO> listCategoryBackVO(ConditionDTO condition) {
        // 查询分类数量
        Long count = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.hasText(condition.getKeyword()), Category::getCategoryName,
                        condition.getKeyword()));
        if (count == 0) {
            return new PageResult<>();
        }
        // 分页查询分类列表
        List<CategoryBackVO> categoryList = categoryMapper.selectCategoryBackVO(PageUtils.getLimit(),
                PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(categoryList, count);
    }

    @Override
    public void addCategory(CategoryDTO category) {
        // 分类是否存在
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, category.getCategoryName()));
        Assert.isNull(existCategory, category.getCategoryName() + "分类已存在");
        // 添加新分类
        Category newCategory = Category.builder()
                .categoryName(category.getCategoryName())
                .build();
        baseMapper.insert(newCategory);
    }

    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        // 分类下是否有文章
        Long count = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList));
        Assert.isFalse(count > 0, "删除失败，分类下存在文章");
        // 批量删除分类
        categoryMapper.deleteBatchIds(categoryIdList);
    }

    @Override
    public void updateCategory(CategoryDTO category) {
        // 分类是否存在
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .select(Category::getId)
                .eq(Category::getCategoryName, category.getCategoryName()));
        Assert.isFalse(Objects.nonNull(existCategory) && !existCategory.getId().equals(category.getId()),
                category.getCategoryName() + "分类已存在");
        // 修改分类
        Category newCategory = Category.builder()
                .id(category.getId())
                .categoryName(category.getCategoryName())
                .build();
        baseMapper.updateById(newCategory);
    }

    @Cacheable(key = "'listCategoryOption'", cacheManager = "caffeineCacheManager", cacheNames = "listCategoryOption")
    @Override
    public List<CategoryOptionVO> listCategoryOption() {
        // 查询分类
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .orderByDesc(Category::getId));
        return BeanCopyUtils.copyBeanList(categoryList, CategoryOptionVO.class);
    }

}
