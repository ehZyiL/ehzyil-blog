package com.ehzyil.service;

import com.ehzyil.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.CategoryDTO;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.CategoryBackVO;
import com.ehzyil.model.vo.front.ArticleConditionList;
import com.ehzyil.model.vo.front.CategoryOptionVO;
import com.ehzyil.model.vo.front.CategoryVO;
import com.ehzyil.model.vo.front.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface ICategoryService extends IService<Category> {

    List<CategoryVO> listCategoryVO();

    /**
     * 查看分类下的文章
     * @param condition
     * @return
     */
    ArticleConditionList listArticleCategory(ConditionDTO condition);


    /**
     * 查看后台分类列表
     *
     * @param condition 查询条件
     * @return 后台分类列表
     */
    PageResult<CategoryBackVO> listCategoryBackVO(ConditionDTO condition);

    /**
     * 添加分类
     *
     * @param category 分类
     */
    void addCategory(CategoryDTO category);

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     */
    void deleteCategory(List<Integer> categoryIdList);

    /**
     * 修改分类
     *
     * @param category 分类
     */
    void updateCategory(CategoryDTO category);


    /**
     * 查看分类选项
     *
     * @return 分类列表
     */
    List<CategoryOptionVO> listCategoryOption();
}
