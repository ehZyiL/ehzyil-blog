package com.ehzyil.utils;

import cn.dev33.satoken.secure.SaSecureUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;


/**
 * 密码加密器，后续接入SpringSecurity之后，可以使用 PasswordEncoder 进行替换

 */
@Component
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


    /**
     * 密码加盐，更推荐的做法是每个用户都使用独立的盐，提高安全性
     */
    @Value("${security.salt}")
    private String salt;

    @Value("${security.salt-index}")
    private Integer saltIndex;

    /**
     * 明文密码处理
     *
     * @param plainPwd
     * @return
     */
    public String encPwd(String plainPwd) {
        if (plainPwd.length() > saltIndex) {
            plainPwd = plainPwd.substring(0, saltIndex) + salt + plainPwd.substring(saltIndex);
        } else {
            plainPwd = plainPwd + salt;
        }
        String md5DigestAsHex = DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8));
        return DigestUtils.md5DigestAsHex(plainPwd.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 校验密码
     * @param plainPwd 未加密的密码
     * @param encPwd 数据库中查询的已加密密码
     * @return
     */
    public  boolean match(String plainPwd, String encPwd) {
        return Objects.equals(encPwd(plainPwd), encPwd);
    }

    public static void main(String[] args) {
        System.out.println(new SecurityUtils().encPwd("123456"));
        //c39211cb8a3e63c83785e904953a03af
    }
}