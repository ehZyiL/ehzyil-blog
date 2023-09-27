package com.ehzyil.service;

import com.ehzyil.domain.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.vo.CategoryVO;

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
}
