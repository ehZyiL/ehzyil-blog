package com.ehzyil.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@TableName("t_menu")
@ApiModel(value="Menu对象", description="")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "父菜单id (paren_id为0且type为M则是一级菜单)")
    private Integer parentId;

    @ApiModelProperty(value = "权限类型 (M目录 C菜单 B按钮)")
    private String menuType;

    @ApiModelProperty(value = "名称")
    private String menuName;

    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "菜单图标")
    private String icon;

    @ApiModelProperty(value = "菜单组件")
    private String component;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "是否隐藏 (0否 1是)")
    private Integer isHidden;

    @ApiModelProperty(value = "是否禁用 (0否 1是)")
    private Integer isDisable;

    @ApiModelProperty(value = "排序")
    private Integer orderNum;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
