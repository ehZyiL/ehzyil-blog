package com.ehzyil.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "评论id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "类型 (1文章 2友链 3说说)")
    private Integer commentType;

    @ApiModelProperty(value = "类型id (类型为友链则没有值)")
    private Integer typeId;

    @ApiModelProperty(value = "父评论id")
    private Integer parentId;

    @ApiModelProperty(value = "回复评论id")
    private Integer replyId;

    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    @ApiModelProperty(value = "评论用户id")
    private Integer fromUid;

    @ApiModelProperty(value = "回复用户id")
    private Integer toUid;

    @ApiModelProperty(value = "是否通过 (0否 1是)")
    private Integer isCheck;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "评论时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
