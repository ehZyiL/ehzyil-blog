package com.ehzyil.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ehzyil.domain.SiteConfig;
import com.ehzyil.domain.User;
import com.ehzyil.domain.UserRole;
import com.ehzyil.model.dto.LoginDTO;
import com.ehzyil.model.dto.MailDTO;
import com.ehzyil.model.dto.RegisterDTO;
import com.ehzyil.service.*;
import com.ehzyil.utils.SecurityUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.ehzyil.constant.CommonConstant.CAPTCHA;
import static com.ehzyil.constant.CommonConstant.USER_NICKNAME;
import static com.ehzyil.constant.RedisConstant.*;
import static com.ehzyil.enums.LoginTypeEnum.EMAIL;
import static com.ehzyil.enums.RoleEnum.USER;
import static com.ehzyil.utils.CommonUtils.checkEmail;
import static java.lang.Boolean.FALSE;

/**
 * @author ehyzil
 * @Description
 * @create 2023-09-2023/9/27-15:28
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private RedisService redisService;
    @Autowired
    private IUserService userService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private EmailService emailService;

    @Override
    public void sendCode(String username) {
        //检验邮箱 失败抛出 IllegalArgumentException
        Assert.isTrue(checkEmail(username),"请输入正确的邮箱！");

        //随机生成验证
        RandomGenerator randomGenerator = new RandomGenerator("0123456789", 6);
        String code = randomGenerator.generate();

        MailDTO mailDTO = MailDTO.builder()
                .toEmail(username)
                .subject(CAPTCHA)
                .content("您的验证码为 " + code + " 有效期为" + CODE_EXPIRE_TIME + "分钟")
                .build();
        //发送邮件
        emailService.sendSimpleMail(mailDTO);

        //TODO 验证码存入消息队列

        // 验证码存入redis
        redisService.setObject(CODE_KEY + username,code,CODE_EXPIRE_TIME, TimeUnit.MINUTES);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void register(RegisterDTO register) {
        //获取验证码
        String code = redisService.getObject(CODE_KEY + register.getUsername());
        //为空
        Assert.notBlank(code,"验证码未发送或已过期！");
        //不一致
        Assert.isTrue(code.equals(register.getCode()), "验证码错误，请重新输入！");

        //邮箱是否注册
        User user = userService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, register.getUsername()));
        Assert.isNull(user,"邮箱已注册！");

        SiteConfig siteConfig = redisService.getObject(SITE_SETTING);
        String pwd = SecurityUtils.sha256Encrypt(register.getPassword());
        //添加用户
        User newUser = User.builder()
                .username(register.getUsername())
                .password(pwd)
                .nickname(USER_NICKNAME + IdWorker.getId())
                .avatar(siteConfig.getTouristAvatar())
                .email(register.getUsername())
                .loginType(EMAIL.getLoginType())
                .isDisable(FALSE)
                .build();

        userService.getBaseMapper().insert(newUser);
        //绑定用户角色
        UserRole userRole = UserRole.builder()
                .userId(newUser.getId())
                .roleId(USER.getRoleId())
                .build();
        userRoleService.getBaseMapper().insert(userRole);
        //删除验证码
      redisService.deleteObject(CODE_KEY + register.getUsername());

    }

    @Override
    public String login(LoginDTO login) {
        User user = userService.getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
                .select(User::getId)
                .eq(User::getUsername, login.getUsername())
                .eq(User::getPassword, SecurityUtils.sha256Encrypt(login.getPassword())));
        Assert.notNull(user,"用户不存在或密码错误");
        // 校验指定账号是否已被封禁，如果被封禁则抛出异常 `DisableServiceException`
        StpUtil.checkDisable(user.getId());
        // 通过校验后，再进行登录
        StpUtil.login(user.getId());
        return StpUtil.getTokenValue();
    }
}
