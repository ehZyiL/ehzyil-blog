package com.ehzyil.domain;

import com.baomidou.mybatisplus.annotation.*;

import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_article")
@ApiModel(value="Article对象", description="")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "文章id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "作者id")
    private Integer userId;

    @ApiModelProperty(value = "分类id")
    private Integer categoryId;

    @ApiModelProperty(value = "缩略图")
    private String articleCover;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    private String articleDescription;

    @ApiModelProperty(value = "文章内容")
    private String articleContent;

    @ApiModelProperty(value = "类型 (1原创 2转载 3翻译)")
    private Integer articleType;

    @ApiModelProperty(value = "是否置顶 (0否 1是）")
    private Integer isTop;

    @ApiModelProperty(value = "是否删除 (0否 1是)")
    private Integer isDelete;

    @ApiModelProperty(value = "是否推荐 (0否 1是)")
    private Integer isRecommend;

    @ApiModelProperty(value = "状态 (1公开 2私密 3评论可见)")
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;


}
