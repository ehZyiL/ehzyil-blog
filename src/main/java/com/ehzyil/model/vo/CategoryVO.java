package com.ehzyil.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ehyzil
 * @Description 分类选项VO
 * @create 2023-09-2023/9/26-15:40
 */
@Data
@ApiModel(description = "分类列表")
public class CategoryVO {

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Integer id;


    /**
     * 分类名
     */
    @ApiModelProperty(value = "分类名")
    private String categoryName;


    /**
     * 文章数量
     */
    @ApiModelProperty(value = "文章数量")
    private Integer articleCount;
}