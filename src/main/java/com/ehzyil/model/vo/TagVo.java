package com.ehzyil.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/26-15:53
 */
@Data
@ApiModel(description = "标签选项VO")
public class TagVo {
    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    private Integer id;

    /**
     * 标签名
     */
    @ApiModelProperty(value = "标签名")
    private String tagName;

    /**
     * 标签数量
     */
    @ApiModelProperty(value = "标签数量")
    private Integer tagCount;
}
