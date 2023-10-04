package com.ehzyil.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ehzyil.domain.Friend;
import com.ehzyil.mapper.FriendMapper;
import com.ehzyil.model.dto.ConditionDTO;
import com.ehzyil.model.dto.FriendDTO;
import com.ehzyil.model.vo.admin.FriendBackVO;
import com.ehzyil.model.vo.front.FriendVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.service.IFriendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.utils.BeanCopyUtils;
import com.ehzyil.utils.PageUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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


    @Override
    public PageResult<FriendBackVO> listFriendBackVO(ConditionDTO condition) {
        // 查询友链数量
        Long count = getBaseMapper().selectCount(new LambdaQueryWrapper<Friend>()
                .like(StringUtils.hasText(condition.getKeyword()), Friend::getName, condition.getKeyword())
        );
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台友链列表
        List<FriendBackVO> friendBackVOList = getBaseMapper().selectFriendBackVOList(PageUtils.getLimit(), PageUtils.getSize(), condition.getKeyword());
        return new PageResult<>(friendBackVOList, count);
    }

    @Override
    public void addFriend(FriendDTO friend) {
        // 新友链
        Friend newFriend = BeanCopyUtils.copyBean(friend, Friend.class);
        // 添加友链
        baseMapper.insert(newFriend);
    }

    @Override
    public void updateFriend(FriendDTO friend) {
        // 新友链
        Friend newFriend = BeanCopyUtils.copyBean(friend, Friend.class);
        // 更新友链
        baseMapper.updateById(newFriend);
    }
}
