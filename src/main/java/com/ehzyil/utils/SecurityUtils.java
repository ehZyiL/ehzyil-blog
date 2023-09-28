package com.ehzyil.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 密码加密
 **/
public class SecurityUtils {


    /**
     * sha256加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    public static String sha256Encrypt(String password) {
        return SaSecureUtil.sha256(password);
    }

    /**
     * 校验密码
     *
     * @param target  旧密码
     * @param target2 新密码
     * @return 是否正确
     */
    public static boolean checkPw(String target, String target2) {
        String encryptedPassword = sha256Encrypt(target2);
        return StringUtils.equals(encryptedPassword, target);
    }

    public static void main(String[] args) {
        String s = SecurityUtils.sha256Encrypt("123456");
        System.out.println(s);

    }
}