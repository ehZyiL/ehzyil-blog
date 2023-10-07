package com.ehzyil.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.Menu;
import com.ehzyil.domain.RoleMenu;
import com.ehzyil.mapper.MenuMapper;
import com.ehzyil.mapper.RoleMenuMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.MenuDTO;
import com.ehzyil.model.vo.admin.MenuOption;
import com.ehzyil.model.vo.admin.MenuTree;
import com.ehzyil.model.vo.admin.MenuVO;
import com.ehzyil.service.IMenuService;
import com.ehzyil.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ehzyil.constant.CommonConstant.PARENT_ID;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public List<MenuVO> listMenuVO(ConditionDTO condition) {
        //查询菜单列表
        List<MenuVO> menuVOList = baseMapper.selectMenuVOList(condition);
        //获取菜单id
        Set<Integer> menuIdList = menuVOList.stream()
                .map(MenuVO::getId)
                .collect(Collectors.toSet());
        //递归查询子菜单
        return menuVOList.stream().map(menuVO -> {
                    //查询父菜单 父菜单id为0
                    Integer parentId = menuVO.getParentId();
                    if (!menuIdList.contains(parentId)) {
                        menuIdList.add(parentId);
                        //父菜单递归查询
                        return recurMenuList(parentId, menuVOList);
                    }
                    //否则返回空列表
                    return new ArrayList<MenuVO>();
                }

        ).collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);
    }

    /**
     * 递归生成菜单列表
     *
     * @param parentId 父菜单id
     * @param menuList 菜单列表
     * @return 菜单列表
     */
    private List<MenuVO> recurMenuList(Integer parentId, List<MenuVO> menuList) {
        return menuList.stream()
                //过滤当前parentId的子菜单id 得到子菜单列表
                .filter(menuVO -> menuVO.getParentId().equals(parentId))
                //递归查询子菜单的 子菜单
                .peek(menuVO -> menuVO.setChildren(recurMenuList(menuVO.getId(), menuList)))
                .collect(Collectors.toList());
    }

    @Override
    public void addMenu(MenuDTO menu) {
        // 名称是否存在
        Menu existMenu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getMenuName, menu.getMenuName()));
        Assert.isNull(existMenu, menu.getMenuName() + "菜单已存在");
        Menu newMenu = BeanCopyUtils.copyBean(menu, Menu.class);
        baseMapper.insert(newMenu);
    }

    @Override
    public void deleteMenu(Integer menuId) {
        // 菜单下是否存在子菜单
        Long menuCount = menuMapper.selectCount(new LambdaQueryWrapper<Menu>()
                .eq(Menu::getParentId, menuId));
        Assert.isFalse(menuCount > 0, "存在子菜单");
        // 菜单是否已分配
        Long roleCount = roleMenuMapper.selectCount(new LambdaQueryWrapper<RoleMenu>()
                .eq(RoleMenu::getMenuId, menuId));
        Assert.isFalse(roleCount > 0, "菜单已分配");
        // 删除菜单
        menuMapper.deleteById(menuId);
    }

    @Override
    public void updateMenu(MenuDTO menu) {
        // 名称是否存在
        Menu existMenu = menuMapper.selectOne(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId)
                .eq(Menu::getMenuName, menu.getMenuName()));
        Assert.isFalse(Objects.nonNull(existMenu) && !existMenu.getId().equals(menu.getId()),
                menu.getMenuName() + "菜单已存在");
        Menu newMenu = BeanCopyUtils.copyBean(menu, Menu.class);
        baseMapper.updateById(newMenu);
    }

    //TODO 待优化
    @Override
    public List<MenuTree> listMenuTree() {
        List<MenuTree> menuTreeList = menuMapper.selectMenuTree();
        return recurMenuTreeList(PARENT_ID, menuTreeList);
    }

    /**
     * 递归生成菜单下拉树
     *
     * @param parentId     父菜单id
     * @param menuTreeList 菜单树列表
     * @return 菜单列表
     */
    private List<MenuTree> recurMenuTreeList(Integer parentId, List<MenuTree> menuTreeList) {
        return menuTreeList.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .peek(menu -> menu.setChildren(recurMenuTreeList(menu.getId(), menuTreeList)))
                .collect(Collectors.toList());
    }

    @Override
    public List<MenuOption> listMenuOption() {
        //查询菜单选项列表
        List<MenuOption> menuOptions = baseMapper.selectMenuOptions();
        return recurMenuOptionList(PARENT_ID, menuOptions);
    }

    /**
     * 递归生成菜单选项树
     *
     * @param parentId       父菜单id
     * @param menuOptionList 菜单树列表
     * @return 菜单列表
     */
    private List<MenuOption> recurMenuOptionList(Integer parentId, List<MenuOption> menuOptionList) {
        return menuOptionList.stream()
                .filter(menuOption -> menuOption.getParentId().equals(parentId))
                .peek(menuOption -> menuOption.setChildren(recurMenuOptionList(menuOption.getValue(), menuOptionList)))
                .collect(Collectors.toList());
    }

    @Override
    public MenuDTO editMenu(Integer menuId) {
        return menuMapper.selectMenuById(menuId);
    }

}
