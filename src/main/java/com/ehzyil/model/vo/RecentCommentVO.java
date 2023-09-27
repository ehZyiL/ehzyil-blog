package com.ehzyil.model.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/26-20:56
 */

@Data
@ApiModel("最新评论VO")
public class RecentCommentVO {

    @ApiModelProperty(value = "评论id")
    private Integer id;

    @ApiModelProperty(value = "评论用户头像")
    private String avatar;
    @ApiModelProperty(value = "评论内容")
    private String commentContent;
    @ApiModelProperty(value = "用户昵称")
    private String nickname;

    @ApiModelProperty(value = "评论时间")
    private LocalDateTime createTime;
}
