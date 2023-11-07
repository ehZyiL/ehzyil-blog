package com.ehzyil.model.converter;

import com.ehzyil.domain.User;
import com.ehzyil.model.dto.BaseUserInfoDTO;
import org.springframework.beans.BeanUtils;

/**
 * @author ehyzil
 * @Description
 * @create 2023-11-2023/11/6-21:00
 */
public class UserConverter {

    public static BaseUserInfoDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        BaseUserInfoDTO baseUserInfoDTO = new BaseUserInfoDTO();
        // todo 知识点，bean属性拷贝的几种方式， 直接get/set方式，使用BeanUtil工具类(spring, cglib, apache, objectMapper)，序列化方式等
        BeanUtils.copyProperties(user, baseUserInfoDTO);

        return baseUserInfoDTO;
    }
}
