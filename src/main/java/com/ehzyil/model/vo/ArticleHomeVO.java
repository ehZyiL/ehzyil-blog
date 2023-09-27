package com.ehzyil.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/25-20:41
 */
@Data
@ApiModel(description = "文章首页VO")
public class ArticleHomeVO {
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

    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章描述")
    private String articleDescription;

    /**
     * 文章分类
     */
    @ApiModelProperty(value = "文章分类")
    private CategoryOptionVO category;

    /**
     * 文章标签
     */
    @ApiModelProperty(value = "文章标签")
    private List<TagOptionVO> tagVOList;

    /**
     * 是否置顶 (0否 1是)
     */
    @ApiModelProperty(value = "是否置顶 (0否 1是)")
    private Integer isTop;

    /**
     * 发表时间
     */
    @ApiModelProperty(value = "发表时间")
    private LocalDateTime createTime;

}