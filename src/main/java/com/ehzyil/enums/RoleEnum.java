package com.ehzyil.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/27-16:11
 */
@Getter
@AllArgsConstructor
public enum RoleEnum {
    /**
     * 管理员
     */
    ADMIN("1", "admin"),
    /**
     * 用户
     */
    USER("2", "user"),
    /**
     * 测试账号
     */
    TEST("3", "test");

    /**
     * 角色id
     */
    private final String roleId;

    /**
     * 描述
     */
    private final String name;
}
