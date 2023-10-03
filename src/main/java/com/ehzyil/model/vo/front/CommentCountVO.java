package com.ehzyil.model.vo.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ehyzil
 * @Description 评论数量VO
 * @create 2023-09-2023/9/26-19:12
 */
@Data
@ApiModel(description = "评论数量VO")
public class CommentCountVO {
    /**
     * 类型id
     */
    @ApiModelProperty(value = "类型id")
    private Integer id;

    /**
     * 评论数量
     */
    @ApiModelProperty(value = "评论数量")
    private Integer commentCount;
}