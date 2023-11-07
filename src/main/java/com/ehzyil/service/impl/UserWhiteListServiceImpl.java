package com.ehzyil.service.impl;

import com.ehzyil.model.dto.BaseUserInfoDTO;
import com.ehzyil.service.IUserService;
import com.ehzyil.service.RedisService;
import com.ehzyil.service.UserWhiteListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author ehyzil
 * @Description
 * @create 2023-11-2023/11/6-20:45
 */
@Service
public class UserWhiteListServiceImpl implements UserWhiteListService {
    /**
     * 实用 redis - set 来存储允许直接发评论的用户
     */
    private static final String USER_WHITE_LIST = "auth_user_white_list";

    @Autowired
    private IUserService userService;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean userInWhiteList(Integer userId) {
        return redisService.hasSetValue(USER_WHITE_LIST, userId);
    }

    @Override
    public List<BaseUserInfoDTO> queryAllWhiteListAuthors() {
        Set<Integer> users = redisService.getSet(USER_WHITE_LIST);
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyList();
        }
        List<BaseUserInfoDTO> userInfos = userService.batchQueryBasicUserInfo(users);
        return userInfos;
    }

    @Override
    public void addUser2WhitList(Integer userId) {
        redisService.setSet(USER_WHITE_LIST, userId);
    }

    @Override
    public void removeUserFromWhiteList(Integer userId) {
        redisService.deleteSet(USER_WHITE_LIST, userId);
    }

    @Override
    public boolean needToReview(Integer userId) {
        return !userInWhiteList(userId);
    }


//    @Override
//    public boolean userInArticleWhiteList(Integer userId) {
//        return RedisClient.setIsMember(USER_WHITE_LIST,userId);
//    }
//
//    @Override
//    public List<BaseUserInfoDTO> queryAllWhiteListAuthors() {
//
//        Set<Integer> users = RedisClient.setGetAll(USER_WHITE_LIST, Integer.class);
//        if (CollectionUtils.isEmpty(users)) {
//            return Collections.emptyList();
//        }
//
//        List<BaseUserInfoDTO> userInfos = userService.batchQueryBasicUserInfo(users);
//        return userInfos;
//    }
//
//    @Override
//    public void addUser2WhitList(Integer userId) {
//        RedisClient.setPut(USER_WHITE_LIST,userId);
//    }
//
//    @Override
//    public void removeUserFromWhiteList(Integer userId) {
//        RedisClient.sDel(USER_WHITE_LIST,userId);
//    }


}
