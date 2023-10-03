package com.ehzyil.model.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author ehyzil
 * @Description
 * @create 2023-10-2023/10/3-21:28
 */
@Data
@Builder
@ApiModel(description = "后台用户登录信息")
public class UserBackInfoVO {


    /**
     * 权限标识
     */
    @ApiModelProperty(value = "权限标识")
    List<String> permissionList;

    /**
     *角色
     */
    @ApiModelProperty(value = "用户角色列表")
    List<String> roleList;
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    private Integer id;
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;


}
