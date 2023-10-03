package com.ehzyil.service;

import com.ehzyil.domain.Friend;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.vo.front.FriendVO;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IFriendService extends IService<Friend> {

    /**
     * 查看友链列表
     *
     * @return 友链列表
     */
    List<FriendVO> listFriendVO();
}
