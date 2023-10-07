package com.ehzyil.mapper;

import com.ehzyil.domain.RoleMenu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    /**
     * 批量删除角色菜单
     *
     * @param roleIdList 需要删除的数据ID
     */
    void deleteRoleMenu(List<String> roleIdList);

    /**
     * 添加角色菜单
     *
     * @param id         角色id
     * @param menuIdList 菜单id集合
     */
    void insertRoleMenu(@Param("roleId") String id, List<Integer> menuIdList);

    /**
     * 根据角色id删除角色菜单
     *
     * @param id 角色id
     */
    void deleteRoleMenuByRoleId(@Param("roleId") String id);

    /**
     * 根据角色id查询菜单权限
     *
     * @param roleId 角色id
     * @return 菜单权限
     */
    List<Integer> selectMenuByRoleId(String roleId);
}
