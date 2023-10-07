package com.ehzyil.mapper;

import com.ehzyil.domain.UserRole;
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
public interface UserRoleMapper extends BaseMapper<UserRole> {


    /**
     * 添加用户角色
     *
     * @param userId     用户id
     * @param roleIdList 角色id集合
     */
    void insertUserRole(@Param("userId") Integer userId, @Param("roleIdList") List<String> roleIdList);
}
