package com.ehzyil.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/27-11:07
 */
@Data
@ApiModel(description = "回复评论VO")
public class CommentReplyVO {


    /**
     * 评论id
     */
    @ApiModelProperty(value = "评论id")
    private Integer id;

    /**
     * 父级评论id
     */
    @ApiModelProperty(value = "父级评论id")
    private Integer parentId;

    /**
     * 评论用户id
     */
    @ApiModelProperty(value = "评论用户id")
    private Integer fromUid;

    /**
     * 被评论用户id
     */
    @ApiModelProperty(value = "被评论用户id")
    private Integer toUid;

    /**
     * 评论用户昵称
     */
    @ApiModelProperty(value = "评论用户昵称")
    private String fromNickname;

    /**
     * 个人网站
     */
    @ApiModelProperty(value = "个人网站")
    private String webSite;

    /**
     * 被评论用户昵称
     */
    @ApiModelProperty(value = "被评论用户昵称")
    private String toNickname;

    /**
     * 头像
     */
    @ApiModelProperty(value = "头像")
    private String avatar;

    /**
     * 评论内容
     */
    @ApiModelProperty(value = "评论内容")
    private String commentContent;

    /**
     * 点赞数
     */
    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;

    /**
     * 评论时间
     */
    @ApiModelProperty(value = "评论时间")
    private LocalDateTime createTime;

}
