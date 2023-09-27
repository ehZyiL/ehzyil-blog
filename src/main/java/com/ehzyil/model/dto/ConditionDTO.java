package com.ehzyil.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/27-11:15
 */

@Data
@ApiModel(description = "查询条件")
public class ConditionDTO {

    /**
     * 页码
     */
    @ApiModelProperty(value = "页码")
    private Long current;

    /**
     * 条数
     */
    @ApiModelProperty(value = "条数")
    private Long size;



    /**
     * 类型id
     */
    @ApiModelProperty(value = "类型id")
    private Integer typeId;

    /**
     * 评论主题类型
     */
    @ApiModelProperty(value = "评论主题类型")
    private Integer commentType;
}
