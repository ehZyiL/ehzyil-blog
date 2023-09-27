package com.ehzyil.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ehyzil
 * @Description 回复数VO
 * @create 2023-09-2023/9/27-12:39
 */
@Data
@ApiModel(description = "回复数VO")
public class CommentReplyCountVO {

    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Integer commentId;

    /**
     * 回复数
     */
    @ApiModelProperty(value = "回复数")
    private Integer replyCount;
}