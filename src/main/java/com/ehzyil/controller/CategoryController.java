package com.ehzyil.controller;


import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.front.ArticleConditionList;
import com.ehzyil.model.vo.front.CategoryVO;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Api(tags = "分类模块")
@RestController
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;

    @ApiOperation(value = "查看分类列表")
    @GetMapping("/category/list")
    public Result<List<CategoryVO>> listCategoryVO() {
        return Result.success(categoryService.listCategoryVO());
    }

    @ApiOperation(value = "查看分类下的文章")
    @GetMapping("/category/article")
    public Result<ArticleConditionList> listArticleCategory(ConditionDTO condition) {
        return Result.success(categoryService.listArticleCategory(condition));
    }
}
