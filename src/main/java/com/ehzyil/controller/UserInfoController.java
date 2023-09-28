package com.ehzyil.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ehzyil.model.dto.EmailDTO;
import com.ehzyil.model.dto.UserInfoDTO;
import com.ehzyil.model.dto.UserPasswordDTO;
import com.ehzyil.model.vo.Result;
import com.ehzyil.model.vo.UserInfoVO;
import com.ehzyil.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/27-17:29
 */
@Api(tags = "用户信息模块")
@RestController
public class UserInfoController {

    @Autowired
    private IUserService userService;

    @SaCheckLogin
    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/user/getUserInfo")
    public Result<UserInfoVO> getUserInfo() {
        return Result.success(userService.getUserInfo());
    }

    /**
     * 修改用户邮箱
     *
     * @param email 邮箱信息
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改用户邮箱")
    @SaCheckPermission(value = "user:email:update")
    @PutMapping("/user/email")
    public Result<?> updateUserEmail(@Validated @RequestBody EmailDTO email) {
        userService.updateUserEmail(email);
        return Result.success();
    }

    @SaCheckLogin
    @ApiOperation(value = "修改用户信息")
    @PutMapping("/user/info")
    public Result<?> updateUserInfo(@RequestBody UserInfoDTO userInfoDTO) {
        userService.updateUserInfo(userInfoDTO);
        return Result.success();
    }
    /**
     * 修改用户密码
     *
     * @param userPasswordDTO
     * @return {@link Result<>}
     */
    @ApiOperation(value = "修改用户密码")
    @PutMapping("/user/password")
    public Result<?> updatePassword(@Validated @RequestBody UserPasswordDTO userPasswordDTO) {
        userService.updatePassword(userPasswordDTO);
        return Result.success();
    }



}
