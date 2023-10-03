package com.ehzyil.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.domain.Category;
import com.ehzyil.model.vo.front.CategoryVO;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface CategoryMapper extends BaseMapper<Category> {
    /**
     * 前端 查询分类列表
     *
     * @return
     */
    List<CategoryVO> selectCategoryVO();
}
