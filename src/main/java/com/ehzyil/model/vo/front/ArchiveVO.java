package com.ehzyil.model.vo.front;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/26-15:00
 */

@Data
@ApiModel(description = "归档VO")
public class ArchiveVO {
    /**
     * 文章id
     */
    @ApiModelProperty(value = "文章id")
    private Integer id;

    @ApiModelProperty(value = "文章标题")
    private String articleTitle;

    @ApiModelProperty(value = "文章缩略图")
    private String articleCover;

    @ApiModelProperty(value = "发表时间")
    private LocalDateTime createTime;
}
