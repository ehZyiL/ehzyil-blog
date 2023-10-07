package com.ehzyil.service;

import com.ehzyil.domain.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.RoleDTO;
import com.ehzyil.model.dto.RoleStatusDTO;
import com.ehzyil.model.vo.admin.RoleVO;
import com.ehzyil.model.vo.front.PageResult;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IRoleService extends IService<Role> {

    /**
     * 查看角色列表
     *
     * @param condition 查询条件
     * @return 角色列表
     */
    PageResult<RoleVO> listRoleVO(ConditionDTO condition);

    /**
     * 添加角色
     *
     * @param role 角色信息
     */
    void addRole(RoleDTO role);

    /**
     * 删除角色
     *
     * @param roleIdList 角色id集合
     */
    void deleteRole(List<String> roleIdList);

    /**
     * 修改角色
     *
     * @param role 角色信息
     */
    void updateRole(RoleDTO role);

    /**
     * 修改角色状态
     *
     * @param roleStatus 角色状态
     */
    void updateRoleStatus(RoleStatusDTO roleStatus);

    /**
     * 查看角色的菜单权限
     *
     * @param roleId 角色id
     * @return 角色的菜单权限
     */
    List<Integer> listRoleMenuTree(String roleId);
}
