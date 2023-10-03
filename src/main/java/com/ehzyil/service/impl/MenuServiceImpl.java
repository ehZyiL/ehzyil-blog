package com.ehzyil.service.impl;

import com.ehzyil.domain.Menu;
import com.ehzyil.mapper.MenuMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.vo.admin.MenuVO;
import com.ehzyil.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Override
    public List<MenuVO> listMenuVO(ConditionDTO condition) {
        return null;
    }
}
