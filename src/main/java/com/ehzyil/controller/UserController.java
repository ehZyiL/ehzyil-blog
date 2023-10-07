package com.ehzyil.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.ehzyil.annotation.OptLogger;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.DisableDTO;
import com.ehzyil.model.dto.PasswordDTO;
import com.ehzyil.model.vo.admin.RouterVO;
import com.ehzyil.model.vo.admin.UserBackVO;
import com.ehzyil.model.vo.admin.UserRoleDTO;
import com.ehzyil.model.vo.admin.UserRoleVO;
import com.ehzyil.model.vo.front.OnlineVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.ehzyil.enums.OptTypeConstant.KICK;
import static com.ehzyil.enums.OptTypeConstant.UPDATE;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@RestController
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 获取登录用户菜单
     *
     * @return {@link RouterVO} 登录用户菜单
     */
    @ApiOperation(value = "获取登录用户菜单")
    @GetMapping("/admin/user/getUserMenu")
    public Result<List<RouterVO>> getUserMenu() {
        return Result.success(userService.getUserMenu());
    }

    /**
     * 查看后台用户列表
     *
     * @param condition 条件
     * @return {@link UserBackVO} 用户后台列表
     */
    @ApiOperation(value = "查看后台用户列表")
    @SaCheckPermission("system:user:list")
    @GetMapping("/admin/user/list")
    public Result<PageResult<UserBackVO>> listUserBackVO(ConditionDTO condition) {
        return Result.success(userService.listUserBackVO(condition));
    }

    /**
     * 查看用户角色选项
     *
     * @return {@link UserRoleVO} 用户角色选项
     */
    @ApiOperation(value = "查看用户角色选项")
    @SaCheckPermission("system:user:list")
    @GetMapping("/admin/user/role")
    public Result<List<UserRoleVO>> listUserRoleDTO() {
        return Result.success(userService.listUserRoleDTO());
    }

    /**
     * 修改用户
     *
     * @param user 用户信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改用户")
    @SaCheckPermission("system:user:update")
    @PutMapping("/admin/user/update")
    public Result<?> updateUser(@Validated @RequestBody UserRoleDTO user) {
        userService.updateUser(user);
        return Result.success();
    }

    /**
     * 修改用户状态
     *
     * @param disable 禁用信息
     * @return {@link Result<>}
     */
    @OptLogger(value = UPDATE)
    @ApiOperation(value = "修改用户状态")
    @SaCheckPermission("system:user:status")
    @PutMapping("/admin/user/changeStatus")
    public Result<?> updateUserStatus(@Validated @RequestBody DisableDTO disable) {
        userService.updateUserStatus(disable);
        return Result.success();
    }

    /**
     * 查看在线用户
     *
     * @param condition 条件
     * @return {@link OnlineVO} 在线用户列表
     */
    @ApiOperation(value = "查看在线用户")
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/admin/online/list")
    public Result<PageResult<OnlineVO>> listOnlineUser(ConditionDTO condition) {
        return Result.success(userService.listOnlineUser(condition));
    }

    /**
     * 下线用户
     *
     * @param token 在线token
     * @return {@link Result<>}
     */
    @OptLogger(value = KICK)
    @ApiOperation(value = "下线用户")
    @SaCheckPermission("monitor:online:kick")
    @GetMapping("/admin/online/kick/{token}")
    public Result<?> kickOutUser(@PathVariable("token") String token) {
        userService.kickOutUser(token);
        return Result.success();
    }

    /**
     * 修改管理员密码
     *
     * @param password 密码
     * @return {@link Result<>}
     */
    @SaCheckRole("1")
    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/password")
    public Result<?> updateAdminPassword(@Validated @RequestBody PasswordDTO password) {
        userService.updateAdminPassword(password);
        return Result.success();
    }
}
