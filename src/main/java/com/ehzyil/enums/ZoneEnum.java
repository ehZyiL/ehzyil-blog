package com.ehzyil.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ehyzil
 * @Description 时区枚举
 * @create 2023-09-2023/9/26-20:41
 */
@Getter
@AllArgsConstructor
public enum ZoneEnum {

    /**
     * 上海
     */
    SHANGHAI("Asia/Shanghai", "中国上海");

    /**
     * 时区
     */
    private final String zone;

    /**
     * 描述
     */
    private final String description;

}