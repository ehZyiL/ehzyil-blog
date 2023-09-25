package com.ehzyil.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@TableName("t_exception_log")
@ApiModel(value="ExceptionLog对象", description="")
public class ExceptionLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "异常id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "异常模块")
    private String module;

    @ApiModelProperty(value = "异常uri")
    private String uri;

    @ApiModelProperty(value = "异常名称")
    private String name;

    @ApiModelProperty(value = "操作描述")
    private String description;

    @ApiModelProperty(value = "异常方法")
    private String errorMethod;

    @ApiModelProperty(value = "异常信息")
    private String message;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "请求方式")
    private String requestMethod;

    @ApiModelProperty(value = "操作ip")
    private String ipAddress;

    @ApiModelProperty(value = "操作地址")
    private String ipSource;

    @ApiModelProperty(value = "操作系统")
    private String os;

    @ApiModelProperty(value = "浏览器")
    private String browser;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;


}
