package com.ehzyil.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.domain.Menu;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.MenuDTO;
import com.ehzyil.model.vo.admin.MenuOption;
import com.ehzyil.model.vo.admin.MenuTree;
import com.ehzyil.model.vo.admin.MenuVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IMenuService extends IService<Menu> {
    /**
     * 查看菜单列表
     *
     * @param condition
     * @return
     */
    List<MenuVO> listMenuVO(ConditionDTO condition);


    /**
     * 添加菜单
     *
     * @param menu 菜单
     */
    void addMenu(MenuDTO menu);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    void deleteMenu(Integer menuId);

    /**
     * 修改菜单
     *
     * @param menu 菜单
     */
    void updateMenu(MenuDTO menu);

    /**
     * 获取菜单下拉树
     *
     * @return 菜单下拉树
     */
    List<MenuTree> listMenuTree();

    /**
     * 获取菜单选项树
     *
     * @return 菜单选项树
     */
    List<MenuOption> listMenuOption();

    /**
     * 编辑菜单
     *
     * @param menuId 菜单id
     * @return 菜单
     */
    MenuDTO editMenu(Integer menuId);
}
