package com.ehzyil.service;

import com.ehzyil.model.dto.LoginDTO;
import com.ehzyil.model.dto.RegisterDTO;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/27-15:27
 */
public interface LoginService {

//    /**
//     * 用户登录
//     *
//     * @param login 登录参数
//     * @return token
//     */
//    String login(LoginDTO login);

    /**
     * 发送验证码
     *
     * @param username 用户名
     */
    void sendCode(String username);

    /**
     * 用户注册
     *
     * @param register 注册信息
     */
    void register(RegisterDTO register);

    String login(LoginDTO login);
}