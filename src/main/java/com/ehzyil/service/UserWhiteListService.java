package com.ehzyil.service;

import com.ehzyil.domain.Comment;
import com.ehzyil.model.dto.BaseUserInfoDTO;

import java.util.List;

public interface UserWhiteListService {

    /**
     * 判断作者是否再文章发布的白名单中；
     * 这个白名单主要是用于控制作者发文章之后是否需要进行审核
     *
     * @param userId
     * @return
     */
    boolean userInWhiteList(Integer userId);

    /**
     * 获取所有的白名单用户
     *
     * @return
     */
    List<BaseUserInfoDTO> queryAllWhiteListAuthors();

    /**
     * 将用户添加到白名单中
     *
     * @param userId
     */
    void addUser2WhitList(Integer userId);

    /**
     * 从白名单中移除用户
     *
     * @param userId
     */
    void removeUserFromWhiteList(Integer userId);

    /**
     * 非白名单的用户，发布的评论需要先进行审核
     *
     * @param comment
     * @return
     */
    boolean needToReview(Integer userId);
}
