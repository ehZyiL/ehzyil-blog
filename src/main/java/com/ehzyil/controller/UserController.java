package com.ehzyil.controller;


import com.ehzyil.model.vo.admin.RouterVO;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
