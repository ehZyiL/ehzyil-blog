package com.ehzyil.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ehyzil
 * @Description 文章上下篇
 * @create 2023-09-2023/9/26-13:20
 */
@Data
@ApiModel(description = "文章上下篇")
public class ArticlePaginationVO {

    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Integer id;

    /**
     * 文章缩略图
     */
    @ApiModelProperty(value = "文章缩略图")
    private String articleCover;

    /**
     * 文章标题
     */
    @ApiModelProperty(value = "文章标题")
    private String articleTitle;
}
