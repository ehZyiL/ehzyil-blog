package com.ehzyil.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.domain.Menu;
import com.ehzyil.model.dto.ConditionDTO;
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
}
