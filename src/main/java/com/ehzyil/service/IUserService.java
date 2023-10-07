package com.ehzyil.service;

import com.ehzyil.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ehzyil.model.dto.*;
import com.ehzyil.model.vo.admin.*;
import com.ehzyil.model.vo.front.OnlineVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.model.vo.front.UserInfoVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

    /**
     * 获取admin登录用户信息
     * @return
     */
    UserBackInfoVO getAdminUserInfo();

    /**
     * 获取登录用户菜单
     * @return
     */
    List<RouterVO> getUserMenu();


    /**
     * 查看后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    PageResult<UserBackVO> listUserBackVO(ConditionDTO condition);

    /**
     * 查看用户角色选项
     *
     * @return 用户角色选项
     */
    List<UserRoleVO> listUserRoleDTO();

    /**
     * 修改用户
     *
     * @param user 用户信息
     */
    void updateUser(UserRoleDTO user);

    /**
     * 修改用户状态
     *
     * @param disable 禁用信息
     */
    void updateUserStatus(DisableDTO disable);

    /**
     * 查看在线用户列表
     *
     * @param condition 条件
     * @return 在线用户列表
     */
    PageResult<OnlineVO> listOnlineUser(ConditionDTO condition);

    /**
     * 下线用户
     *
     * @param token 在线token
     */
    void kickOutUser(String token);

    /**
     * 修改管理员密码
     *
     * @param password 密码
     */
    void updateAdminPassword(PasswordDTO password);
}
