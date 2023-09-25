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
@TableName("t_operation_log")
@ApiModel(value="OperationLog对象", description="")
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "操作id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "操作模块")
    private String module;

    @ApiModelProperty(value = "操作类型")
    private String type;

    @ApiModelProperty(value = "操作uri")
    private String uri;

    @ApiModelProperty(value = "方法名称")
    private String name;

    @ApiModelProperty(value = "操作描述")
    private String description;

    @ApiModelProperty(value = "请求参数")
    private String params;

    @ApiModelProperty(value = "请求方式")
    private String method;

    @ApiModelProperty(value = "返回数据")
    private String data;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "操作ip")
    private String ipAddress;

    @ApiModelProperty(value = "操作地址")
    private String ipSource;

    @ApiModelProperty(value = "操作耗时 (毫秒)")
    private Integer times;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;


}
