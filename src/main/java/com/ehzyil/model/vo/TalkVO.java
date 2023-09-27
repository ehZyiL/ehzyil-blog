package com.ehzyil.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/26-16:40
 */
@Data
@ApiModel(description = "说说列表")
public class TalkVO {

    /**
     * 标签id
     */
    @ApiModelProperty(value = "标签id")
    private Integer id;

    /**
     * 标签名
     */
    @ApiModelProperty(value = "标签名")
    private String nickname;

    @ApiModelProperty(value = "头像地址")
    private String avatar;
    @ApiModelProperty(value = "说说内容")
    private String talkContent;

    @JsonIgnore
    @ApiModelProperty(value = "图片")
    private String images;

    @ApiModelProperty(value = "说说图片列表")
    private List<String> imgList;
    @ApiModelProperty(value = "是否置顶")
    private Integer isTop;
    @ApiModelProperty(value = "点赞数")
    private Integer likeCount;
    @ApiModelProperty(value = "评论量")
    private Integer commentCount;
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
