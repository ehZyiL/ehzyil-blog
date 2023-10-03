package com.ehzyil.service.impl;

import com.ehzyil.domain.Friend;
import com.ehzyil.mapper.FriendMapper;
import com.ehzyil.model.vo.front.FriendVO;
import com.ehzyil.service.IFriendService;
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
public class FriendServiceImpl extends ServiceImpl<FriendMapper, Friend> implements IFriendService {

    @Override
    public List<FriendVO> listFriendVO() {
        return getBaseMapper(). selectFriendVOList();
    }
}
