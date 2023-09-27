package com.ehzyil.controller;


import com.ehzyil.model.vo.CategoryOptionVO;
import com.ehzyil.model.vo.CategoryVO;
import com.ehzyil.model.vo.Result;
import com.ehzyil.service.ICategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
