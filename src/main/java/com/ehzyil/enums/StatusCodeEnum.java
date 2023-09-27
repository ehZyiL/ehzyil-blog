package com.ehzyil.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/25-20:12
 */
@Getter
@AllArgsConstructor
public enum StatusCodeEnum {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),

    /**
     * 参数错误
     */
    VALID_ERROR(400, "参数错误"),

    /**
     * 未登录
     */
    UNAUTHORIZED(402, "未登录"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(-1, "系统异常"),

    /**
     * 操作失败
     */
    FAIL(500, "操作失败");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 返回信息
     */
    private final String msg;

}