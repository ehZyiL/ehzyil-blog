package com.ehzyil.mapper;

import com.ehzyil.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.UserBackVO;
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
public interface UserMapper extends BaseMapper<User> {

    /**
     * 查询用户后台数量
     *
     * @param condition 条件
     * @return 用户数量
     */
    Long countUser(@Param("condition") ConditionDTO condition);

    /**
     * 查询后台用户列表
     *
     * @param limit     页码
     * @param size      大小
     * @param condition 条件
     * @return 用户后台列表
     */
    List<UserBackVO> listUserBackVO(@Param("limit") Long limit, @Param("size") Long size, @Param("condition") ConditionDTO condition);
}
