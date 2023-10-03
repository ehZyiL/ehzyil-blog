package com.ehzyil.model.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ehyzil
 * @Description
 * @create 2023-10-2023/10/3-21:51
 */
@Data
@Builder
@ApiModel(description = "用户菜单信息")
public class MenuVO {

    @ApiModelProperty(value = "子菜单列表")
    List<String> children;

    @ApiModelProperty(value = "菜单组件")
    private String component;

    @ApiModelProperty(value = "权限标识")
    private String icon;

    @ApiModelProperty(value = "权限标识")
    private String id;

    @ApiModelProperty(value = "菜单名称")
    private String menuName;

    @ApiModelProperty(value = "类型（M目录 C菜单 B按钮）")
    private String menuType;

    @ApiModelProperty(value = "orderNum")
    private String orderNum;

    @ApiModelProperty(value = "路由地址")
    private String path;

    @ApiModelProperty(value = "权限标识")
    private String perms;

    @ApiModelProperty(value = "父级菜单id")
    private String parentId;

    @ApiModelProperty(value = "是否隐藏 (0否 1是)")
    private String isHidden;

    @ApiModelProperty(value = "是否禁用 (0否 1是)")
    private String isDisable;

    @ApiModelProperty(value = "创建时间")
    private String createTime;


}
