package com.ehzyil.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
@Description 文章推荐VO
@author ehyzil
@create 2023-09-2023/9/26-12:41
*/
@Data
@ApiModel(description = "文章推荐VO")
public class ArticleRecommendVO {
    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Integer id;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章缩略图")
    private String articleCover;

    @ApiModelProperty(value = "发表时间")
    private LocalDateTime createTime;
}
