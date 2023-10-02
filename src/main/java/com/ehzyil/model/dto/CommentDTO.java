package com.ehzyil.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ehyzil
 * @Description
 * @create 2023-10-2023/10/2-20:42
 */
@Data
@ApiModel(description = "评论参数")
public class CommentDTO {

    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    @ApiModelProperty(value = "评论类型 (1文章 2友链 3说说)")
    private Integer commentType;

    @ApiModelProperty(value = "父评论id")
    private Integer parentId;

    @ApiModelProperty(value = "被回复评论id")
    private Integer replyId;

    @ApiModelProperty(value = "被回复用户id")
    private Integer toUid;

    @ApiModelProperty(value = "类型id")
    private Integer typeId;


}
