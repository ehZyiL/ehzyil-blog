package com.ehzyil.model.vo.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/26-20:03
 */
@Data
@ApiModel("留言VO")
public class MessageVO {

    @ApiModelProperty(value = "用户头像")
    private String avatar;

    @ApiModelProperty(value = "留言id")
    private Integer id;

    @ApiModelProperty(value = "留言内容")
    private String messageContent;

    @ApiModelProperty(value = "昵称")
    private String nickname;
}
