package com.ehzyil.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.domain.Category;
import com.ehzyil.model.vo.admin.CategoryBackVO;
import com.ehzyil.model.vo.front.CategoryVO;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 查询后台分类列表
     *
     * @param limit   页码
     * @param size    大小
     * @param keyword 关键字
     * @return 后台分类列表
     */
    List<CategoryBackVO> selectCategoryBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("keyword") String keyword);

}
