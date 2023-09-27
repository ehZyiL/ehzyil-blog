package com.ehzyil.service.impl;

import com.ehzyil.domain.Category;
import com.ehzyil.mapper.CategoryMapper;
import com.ehzyil.model.vo.CategoryVO;
import com.ehzyil.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public List<CategoryVO> listCategoryVO() {
        return categoryMapper.selectCategoryVO();
    }
}
