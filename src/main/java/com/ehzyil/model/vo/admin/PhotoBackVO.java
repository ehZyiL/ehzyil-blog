package com.ehzyil.model.vo.admin;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 后台照片VO
 **/
@Data
@ApiModel(description = "后台照片VO")
public class PhotoBackVO {

    /**
     * 照片id
     */
    @ApiModelProperty(value = "照片id")
    private Integer id;

    /**
     * 照片名
     */
    @ApiModelProperty(value = "照片名")
    private String photoName;

    /**
     * 照片描述
     */
    @ApiModelProperty(value = "照片描述")
    private String photoDesc;

    /**
     * 照片地址
     */
    @ApiModelProperty(value = "照片地址")
    private String photoUrl;
}