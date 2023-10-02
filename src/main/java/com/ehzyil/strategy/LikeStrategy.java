package com.ehzyil.strategy;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author ehyzil
 * @Description
 * @create 2023-10-2023/10/2-22:17
 */
public interface LikeStrategy {

    /**
     * 点赞
     *
     * @param typeId 类型id
     */
    void like(Integer typeId);
}
