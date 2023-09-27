package com.ehzyil.controller;


import com.ehzyil.model.vo.CategoryVO;
import com.ehzyil.model.vo.Result;
import com.ehzyil.model.vo.TagVo;
import com.ehzyil.service.ICategoryService;
import com.ehzyil.service.ITagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(tags = "标签模块")
@RestController

public class TagController {

    @Autowired
    ITagService tagService;

    @ApiOperation(value = "查看标签列表")
    @GetMapping("/tag/list")
    public Result<List<TagVo>> listTagVO() {
        return Result.success(tagService.listTagVO());
    }

}
