package com.ehzyil.model.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@ApiModel("用户基础实体对象")
public class BaseUserInfoDTO extends BaseDTO {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id", required = true)
    private Integer id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名", required = true)
    private String username;


    /**
     * 用户图像
     */
    @ApiModelProperty(value = "用户头像")
    private String avatar;

    /**
     * 个人网站
     */
    @ApiModelProperty(value = "个人网站")
    private String webSite;

    /**
     * 个人简介
     */
    @ApiModelProperty(value = "个人简介")
    private String intro;



    /**
     * 用户最后登录ip
     */
    @ApiModelProperty(value = "用户最后登录的ip", example = "湖北·武汉")
    private String ipAddress;


    /**
     * 用户最后登录区域
     */
    @ApiModelProperty(value = "用户最后登录的地理位置", example = "湖北·武汉")
    private String ipSource;
}
