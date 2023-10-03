package com.ehzyil.model.vo.front;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author ehyzil
 * @Description 分页返回类
 * @create 2023-09-2023/9/25-20:34
 */


@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页返回类")
public class PageResult<T> {

    /**
     * 分页结果
     */
    @ApiModelProperty(value = "分页结果")
    private List<T> recordList;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总数", dataType = "long")
    private Long count;

}