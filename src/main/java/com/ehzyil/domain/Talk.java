package com.ehzyil.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_talk")
@ApiModel(value="Talk对象", description="")
public class Talk implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "说说id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "说说内容")
    private String talkContent;

    @ApiModelProperty(value = "说说图片")
    private String images;

    @ApiModelProperty(value = "是否置顶 (0否 1是)")
    private Boolean isTop;

    @ApiModelProperty(value = "状态 (1公开  2私密)")
    private Boolean status;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
