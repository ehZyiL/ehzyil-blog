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
@TableName("t_site_config")
@ApiModel(value="SiteConfig对象", description="")
public class SiteConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "用户头像")
    private String userAvatar;

    @ApiModelProperty(value = "游客头像")
    private String touristAvatar;

    @ApiModelProperty(value = "网站名称")
    private String siteName;

    @ApiModelProperty(value = "网站地址")
    private String siteAddress;

    @ApiModelProperty(value = "网站简介")
    private String siteIntro;

    @ApiModelProperty(value = "网站公告")
    private String siteNotice;

    @ApiModelProperty(value = "建站日期")
    private String createSiteTime;

    @ApiModelProperty(value = "备案号")
    private String recordNumber;

    @ApiModelProperty(value = "作者头像")
    private String authorAvatar;

    @ApiModelProperty(value = "网站作者")
    private String siteAuthor;

    @ApiModelProperty(value = "文章默认封面")
    private String articleCover;

    @ApiModelProperty(value = "关于我")
    private String aboutMe;

    @ApiModelProperty(value = "Github")
    private String github;

    @ApiModelProperty(value = "Gitee")
    private String gitee;

    @ApiModelProperty(value = "哔哩哔哩")
    private String bilibili;

    @ApiModelProperty(value = "QQ")
    private String qq;

    @ApiModelProperty(value = "是否评论审核 (0否 1是)")
    private Boolean commentCheck;

    @ApiModelProperty(value = "是否留言审核 (0否 1是)")
    private Boolean messageCheck;

    @ApiModelProperty(value = "是否开启打赏 (0否 1是)")
    private Boolean isReward;

    @ApiModelProperty(value = "微信二维码")
    private String weiXinCode;

    @ApiModelProperty(value = "支付宝二维码")
    private String aliCode;

    @ApiModelProperty(value = "是否邮箱通知 (0否 1是)")
    private Boolean emailNotice;

    @ApiModelProperty(value = "社交列表")
    private String socialList;

    @ApiModelProperty(value = "登录方式")
    private String loginList;

    @ApiModelProperty(value = "是否开启音乐播放器 (0否 1是)")
    private Boolean isMusic;

    @ApiModelProperty(value = "网易云歌单id")
    private String musicId;

    @ApiModelProperty(value = "是否开启聊天室 (0否 1是)")
    private Boolean isChat;

    @ApiModelProperty(value = "websocket链接")
    private String websocketUrl;

    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
