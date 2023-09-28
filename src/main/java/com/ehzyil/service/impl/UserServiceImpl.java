package com.ehzyil.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.User;
import com.ehzyil.mapper.UserMapper;
import com.ehzyil.model.dto.EmailDTO;
import com.ehzyil.model.dto.UserInfoDTO;
import com.ehzyil.model.dto.UserPasswordDTO;
import com.ehzyil.model.vo.UserInfoVO;
import com.ehzyil.service.IUserService;
import com.ehzyil.service.RedisService;
import com.ehzyil.strategy.context.UploadStrategyContext;
import com.ehzyil.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.ehzyil.constant.RedisConstant.CODE_KEY;
import static com.ehzyil.enums.FilePathEnum.AVATAR;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ehzyil
 * @since 2023-09-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Autowired
    private RedisService redisService;


    @Autowired
    private UploadStrategyContext uploadStrategyContext;

    @Override
    public UserInfoVO getUserInfo() {
        Integer userId = StpUtil.getLoginIdAsInt();
        User user = getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
                .select(User::getNickname, User::getAvatar, User::getUsername, User::getWebSite,
                        User::getIntro, User::getEmail, User::getLoginType)
                .eq(User::getId, userId));

        return UserInfoVO
                .builder()
                .id(userId)
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .email(user.getEmail())
                .webSite(user.getWebSite())
                .intro(user.getIntro())
//                .articleLikeSet(articleLikeSet)
//                .commentLikeSet(commentLikeSet)
//                .talkLikeSet(talkLikeSet)
                .loginType(user.getLoginType())
                .build();
    }

    @Override
    public void updateUserInfo(UserInfoDTO userInfoDTO) {
        User updateUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .nickname(userInfoDTO.getNickname())
                .intro(userInfoDTO.getIntro())
                .webSite(userInfoDTO.getWebSite())
                .build();
        getBaseMapper().updateById(updateUser);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateUserEmail(EmailDTO email) {
        verifyCode(email.getEmail(), email.getCode());
        User newUser = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .email(email.getEmail())
                .build();
        getBaseMapper().updateById(newUser);

        //删除验证码
        redisService.deleteObject(CODE_KEY + email.getEmail());
    }

    @Override
    public void updatePassword(UserPasswordDTO userPasswordDTO) {
        verifyCode(userPasswordDTO.getUsername(), userPasswordDTO.getCode());
        //查询是否注册
        User existUser = getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
                .select(User::getUsername)
                .eq(User::getUsername, userPasswordDTO.getUsername()));
        Assert.notNull(existUser, "邮箱尚未注册！");

        // 根据用户名修改密码
        getBaseMapper().update(new User(), new LambdaUpdateWrapper<User>()
                .set(User::getPassword, SecurityUtils.sha256Encrypt(userPasswordDTO.getPassword()))
                .eq(User::getUsername, userPasswordDTO.getUsername()));
    }

    @Override
    public String updateAvatar(MultipartFile multipartFile) {
        //头像上传
        String avatar=uploadStrategyContext.executeUploadStrategy(multipartFile,AVATAR.getPath());

        //更新用户头像
        User user=User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .avatar(avatar)
                .build();
        getBaseMapper().updateById(user);
        return avatar;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     */
    public void verifyCode(String username, String code) {
        String sysCode = redisService.getObject(CODE_KEY + username);
        Assert.notBlank(sysCode, "验证码未发送或已过期！");
        Assert.isTrue(sysCode.equals(code), "验证码错误，请重新输入！");
    }


}
