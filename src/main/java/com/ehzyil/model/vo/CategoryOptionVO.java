package com.ehzyil.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/26-9:45
 */
@Data
@ApiModel(description = "分类选项VO")
public class CategoryOptionVO {

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
}