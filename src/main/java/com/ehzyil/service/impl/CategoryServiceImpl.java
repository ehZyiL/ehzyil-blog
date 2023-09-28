package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.Category;
import com.ehzyil.mapper.ArticleMapper;
import com.ehzyil.mapper.CategoryMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.ArticleConditionList;
import com.ehzyil.model.vo.ArticleConditionVO;
import com.ehzyil.model.vo.CategoryVO;
import com.ehzyil.service.ICategoryService;
import com.ehzyil.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
