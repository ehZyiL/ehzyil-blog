package com.ehzyil.service;

import com.ehzyil.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.EmailDTO;
import com.ehzyil.model.dto.UserInfoDTO;
import com.ehzyil.model.dto.UserPasswordDTO;
import com.ehzyil.model.vo.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
public interface IUserService extends IService<User> {
    /**
     * 获取登录用户信息
     * @return
     */
    UserInfoVO getUserInfo();

    /**
     * 更新用户信息
     * @param userInfoDTO
     */
    void updateUserInfo(UserInfoDTO userInfoDTO);

    void updateUserEmail(EmailDTO email);

    void updatePassword(UserPasswordDTO userPasswordDTO);

    String updateAvatar(MultipartFile multipartFile);
}
