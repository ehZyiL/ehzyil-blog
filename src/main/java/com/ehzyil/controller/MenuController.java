package com.ehzyil.controller;


import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.MenuVO;
import com.ehzyil.model.vo.front.Result;
import com.ehzyil.service.IMenuService;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
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

public class MenuController {

        @Autowired
        private IMenuService menuService;

        /**
         * 查看菜单列表
         *
         * @return {@link MenuVO} 菜单列表
         */
        @ApiOperation(value = "查看菜单列表")
        @SaCheckPermission("system:menu:list")
        @GetMapping("/admin/menu/list")
        public Result<List<MenuVO>> listMenuVO(ConditionDTO condition) {
                return Result.success(menuService.listMenuVO(condition));
        }

}
