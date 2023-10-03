package com.ehzyil.model.vo.front;

/**
 * @author ehyzil
 * @Description 标签选项VO
 * @create 2023-09-2023/9/25-20:42
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "标签选项VO")
public class TagOptionVO {

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
}