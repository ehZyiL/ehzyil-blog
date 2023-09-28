package com.ehzyil.domain;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@TableName("t_user")
@Builder
@ApiModel(value = "User对象", description = "")
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "头像")
    private String avatar;

    @ApiModelProperty(value = "个人网站")
    private String webSite;

    @ApiModelProperty(value = "个人简介")
    private String intro;

    @ApiModelProperty(value = "邮箱")
    private String email;

    @ApiModelProperty(value = "登录ip")
    private String ipAddress;

    @ApiModelProperty(value = "登录地址")
    private String ipSource;

    @ApiModelProperty(value = "登录方式 (1邮箱 2QQ 3Gitee 4Github)")
    private Integer loginType;

    @ApiModelProperty(value = "是否禁用 (0否 1是)")
    private Boolean isDisable;

    @ApiModelProperty(value = "登录时间")
    private LocalDateTime loginTime;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
