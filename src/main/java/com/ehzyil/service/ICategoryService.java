package com.ehzyil.service;

import com.ehzyil.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.front.ArticleConditionList;
import com.ehzyil.model.vo.front.CategoryVO;

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
}
