package com.ehzyil.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import com.ehzyil.model.dto.LoginDTO;
import com.ehzyil.model.dto.RegisterDTO;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/27-15:25
 */
@RestController
@Api(tags = "用户登陆模块")
public class LoginController {
    @Autowired
    private LoginService loginService;


//    @AccessLimit(seconds = 60, maxCount = 1)
    @ApiOperation(value = "发送邮箱验证码")
    @GetMapping("/code")
    Result<?> sendCode( String username){
        loginService.sendCode(username);
        return  Result.success();
    }

    @ApiOperation(value = "用户邮箱注册")
    @PostMapping("/register")
    public Result<?> register(@Validated @RequestBody RegisterDTO register) {
        loginService.register(register);
        return Result.success();
    }


    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public Result<String> login(@Validated @RequestBody LoginDTO login) {
        return Result.success(loginService.login(login));
    }
    /**
     * 用户退出
     */
    @SaCheckLogin
    @ApiOperation(value = "用户退出")
    @GetMapping("/logout")
    public Result<?> logout() {
        StpUtil.logout();
        return Result.success();
    }

    //TODO 第三方登录




}
