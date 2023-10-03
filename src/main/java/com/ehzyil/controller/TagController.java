package com.ehzyil.controller;


import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.front.ArticleConditionList;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.model.vo.front.TagVo;
import com.ehzyil.service.ITagService;
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

    /**
     * 查看标签下的文章
     *
     * @param condition 查询条件
     * @return 文章列表
     */

    @ApiOperation(value = "查看标签下的文章")
    @GetMapping("/tag/article")
    public Result<ArticleConditionList> listArticleTag(ConditionDTO condition) {
        return Result.success(tagService.listArticleTag(condition));
    }

}
