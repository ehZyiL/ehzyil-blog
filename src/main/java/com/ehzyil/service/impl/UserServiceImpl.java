package com.ehzyil.service.impl;

import cn.dev33.satoken.session.SaSession;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.BlogFile;
import com.ehzyil.domain.User;
import com.ehzyil.domain.UserRole;
import com.ehzyil.exception.ServiceException;
import com.ehzyil.mapper.*;
import com.ehzyil.model.converter.UserConverter;
import com.ehzyil.model.dto.*;
import com.ehzyil.model.vo.admin.*;
import com.ehzyil.model.vo.front.OnlineVO;
import com.ehzyil.model.vo.front.PageResult;
import com.ehzyil.model.vo.front.UserInfoVO;
import com.ehzyil.service.IUserService;
import com.ehzyil.service.RedisService;
import com.ehzyil.strategy.context.UploadStrategyContext;
import com.ehzyil.utils.FileUtils;
import com.ehzyil.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.ehzyil.constant.CommonConstant.*;
import static com.ehzyil.constant.RedisConstant.*;
import static com.ehzyil.enums.FilePathEnum.AVATAR;
import static com.ehzyil.enums.FilePathEnum.CONFIG;
import static com.ehzyil.utils.PageUtils.getLimit;
import static com.ehzyil.utils.PageUtils.getSize;

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

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private BlogFileMapper blogFileMapper;

    @Autowired
    private UserRoleMapper  userRoleMapper;


    @Override
    public UserInfoVO getUserInfo() {
        Integer userId = StpUtil.getLoginIdAsInt();
        User user = getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
                .select(User::getNickname, User::getAvatar, User::getUsername, User::getWebSite,
                        User::getIntro, User::getEmail, User::getLoginType)
                .eq(User::getId, userId));
        //获取用户点赞集合
        Set<Object> articleLikeSet = redisService.getSet(USER_ARTICLE_LIKE + userId);
        Set<Object> commentLikeSet = redisService.getSet(USER_COMMENT_LIKE + userId);
        Set<Object> talkLikeSet = redisService.getSet(USER_TALK_LIKE + userId);

        return UserInfoVO
                .builder()
                .id(userId)
                .avatar(user.getAvatar())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .email(user.getEmail())
                .webSite(user.getWebSite())
                .intro(user.getIntro())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .talkLikeSet(talkLikeSet)
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
    public String updateAvatar(MultipartFile file) {
        //头像上传
        String url = uploadStrategyContext.executeUploadStrategy(file, AVATAR.getFilePath());

        //更新用户头像
        User user = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .avatar(url)
                .build();
        getBaseMapper().updateById(user);

        return url;
    }

    @Override
    public UserBackInfoVO getAdminUserInfo() {
        //获取登录id
        int userId = StpUtil.getLoginIdAsInt();
        //查询用户头像
        User user = getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
                .select(User::getAvatar).eq(User::getId, userId));
        //查询用户角色列表
//        List<String> roleIdList = roleMapper.selectRoleListByUserId(userId);
        List<String> roleIdList = StpUtil.getRoleList();
        // 声明权限码集合
//        List<String> permissionList = new ArrayList<>();

        //根据角色查询用户权限列表
//        roleIds.forEach(roleId->permissionList.addAll(menuMapper.selectPermissionByRoleId(roleId)));
        List<String> permissionList = StpUtil.getPermissionList().stream()
                .filter(string -> !string.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        return UserBackInfoVO.builder()
                .id(userId)
                .avatar(user.getAvatar())
                .roleList(roleIdList)
                .permissionList(permissionList).build();
    }

    @Override
    public List<RouterVO> getUserMenu() {

        // 查询用户菜单
        List<UserMenuVO> userMenuVOList = menuMapper.selectMenuByUserId(StpUtil.getLoginIdAsInt());
        //查询子菜单  递归生成路由,parentId为0

        return recurRoutes(PARENT_ID, userMenuVOList);
    }



    /**
     * 递归生成路由列表
     *
     * @param parentId 父级ID
     * @param menuList 菜单列表
     * @return 路由列表
     */
    private List<RouterVO> recurRoutes(Integer parentId, List<UserMenuVO> menuList) {
        List<RouterVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).ifPresent(menus -> menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .forEach(menu -> {
                    RouterVO routeVO = new RouterVO();
                    routeVO.setName(menu.getMenuName());
                    routeVO.setPath(getRouterPath(menu));
                    routeVO.setComponent(getComponent(menu));
                    routeVO.setMeta(MetaVO.builder()
                            .title(menu.getMenuName())
                            .icon(menu.getIcon())
                            .hidden(menu.getIsHidden().equals(TRUE))
                            .build());

                    if (menu.getMenuType().equals(TYPE_DIR)) {
                        List<RouterVO> children = recurRoutes(menu.getId(), menuList);
                        if (CollectionUtil.isNotEmpty(children)) {
                            routeVO.setAlwaysShow(true);
                            routeVO.setRedirect("noRedirect");
                        }
                        routeVO.setChildren(children);
                    } else if (isMenuFrame(menu)) {
                        routeVO.setMeta(null);
                        List<RouterVO> childrenList = new ArrayList<>();
                        RouterVO children = new RouterVO();
                        children.setName(menu.getMenuName());
                        children.setPath(menu.getPath());
                        children.setComponent(menu.getComponent());
                        children.setMeta(MetaVO.builder()
                                .title(menu.getMenuName())
                                .icon(menu.getIcon())
                                .hidden(menu.getIsHidden().equals(TRUE))
                                .build());
                        childrenList.add(children);
                        routeVO.setChildren(childrenList);
                    }
                    list.add(routeVO);
                }));
        return list;
    }

    /**
     * 获取路由地址
     *
     * @param menu 菜单信息
     * @return 路由地址
     */
    private String getRouterPath(UserMenuVO menu) {
        String routerPath = menu.getPath();
        //一级目录
        if (menu.getParentId().equals(PARENT_ID) && TYPE_DIR.equals(menu.getMenuType())) {
            routerPath = "/" + routerPath;
        } else if (isMenuFrame(menu)) {
            routerPath = "/";
        }
        return routerPath;
    }

    /**
     * 获取组件信息
     *
     * @param menu 菜单信息
     * @return 组件信息
     */
    public String getComponent(UserMenuVO menu) {
        String component = LAYOUT;
        if (StringUtils.isNotEmpty(menu.getComponent()) && !isMenuFrame(menu)) {
            component = menu.getComponent();
        } else if (StringUtils.isEmpty(menu.getComponent()) && isParentView(menu)) {
            component = PARENT_VIEW;
        }
        return component;
    }

    /**
     * 是否为菜单内部跳转
     *
     * @param menu 菜单信息
     * @return 结果
     */
    private boolean isMenuFrame(UserMenuVO menu) {
        return menu.getParentId().equals(PARENT_ID) && TYPE_MENU.equals(menu.getMenuType());
    }

    /**
     * 是否为parent_view组件
     *
     * @param menu 菜单信息
     * @return 结果
     */
    public boolean isParentView(UserMenuVO menu) {
        return !menu.getParentId().equals(PARENT_ID) && TYPE_DIR.equals(menu.getMenuType());
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


    @Override
    public PageResult<UserBackVO> listUserBackVO(ConditionDTO condition) {
        // 查询后台用户数量
        Long count = getBaseMapper().countUser(condition);
        if (count == 0) {
            return new PageResult<>();
        }
        // 查询后台用户列表
        List<UserBackVO> userBackVOList = getBaseMapper().listUserBackVO(getLimit(), getSize(), condition);
        return new PageResult<>(userBackVOList, count);
    }


    @Override
    public List<UserRoleVO> listUserRoleDTO() {
        // 查询角色列表
        return roleMapper.selectUserRoleList();
    }

    @Override
    public void updateUser(UserRoleDTO user) {
        // 更新用户信息
        User newUser = User.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .build();
        baseMapper.updateById(newUser);
        // 删除用户角色
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, user.getId()));
        // 重新添加用户角色
        userRoleMapper.insertUserRole(user.getId(), user.getRoleIdList());
        // 删除Redis缓存中的角色
        SaSession sessionByLoginId = StpUtil.getSessionByLoginId(user.getId(), false);
        Optional.ofNullable(sessionByLoginId).ifPresent(saSession -> saSession.delete("Role_List"));
    }

    @Override
    public void updateUserStatus(DisableDTO disable) {
        // 更新用户状态
        User newUser = User.builder()
                .id(disable.getId())
                .isDisable(disable.getIsDisable())
                .build();
        getBaseMapper().updateById(newUser);
        if (disable.getIsDisable().equals(TRUE)) {
            // 先踢下线
            StpUtil.logout(disable.getId());
            // 再封禁账号
            StpUtil.disable(disable.getId(), 86400);
        } else {
            // 解除封禁
            StpUtil.untieDisable(disable.getId());
        }
    }



    @Override
    public void updateAdminPassword(PasswordDTO password) {
        Integer userId = StpUtil.getLoginIdAsInt();
        // 查询旧密码是否正确
        User user = getBaseMapper().selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getId, userId));
        Assert.notNull(user, "用户不存在");
        Assert.isTrue(SecurityUtils.checkPw(user.getPassword(), password.getOldPassword()), "旧密码校验不通过!");
        // 正确则修改密码
        String newPassword = SecurityUtils.sha256Encrypt(password.getNewPassword());
        user.setPassword(newPassword);
        getBaseMapper().updateById(user);
    }

    @Override
    public List<BaseUserInfoDTO> batchQueryBasicUserInfo(Collection<Integer> userIds) {
        List<User> userList = getBaseMapper().selectBatchIds(userIds);
        if (CollectionUtils.isEmpty(userList)) {
            throw new ServiceException("");
        }
        return userList.stream().map(UserConverter::toDTO).collect(Collectors.toList());
    }

    @Override
    public PageResult<OnlineVO> listOnlineUser(ConditionDTO condition) {
        // 查询所有会话token
        List<String> tokenList = StpUtil.searchTokenSessionId("", 0, -1, false);
        List<OnlineVO> onlineVOList = tokenList.stream()
                .map(token -> {
                    // 获取tokenSession
                    SaSession sessionBySessionId = StpUtil.getSessionBySessionId(token);
                    return (OnlineVO) sessionBySessionId.get(ONLINE_USER);
                })
                .filter(onlineVO -> StringUtils.isEmpty(condition.getKeyword()) || onlineVO.getNickname().contains(condition.getKeyword()))
                .sorted(Comparator.comparing(OnlineVO::getLoginTime).reversed())
                .collect(Collectors.toList());
        // 执行分页
        int fromIndex = getLimit().intValue();
        int size = getSize().intValue();
        int toIndex = onlineVOList.size() - fromIndex > size ? fromIndex + size : onlineVOList.size();
        List<OnlineVO> userOnlineList = onlineVOList.subList(fromIndex, toIndex);
        return new PageResult<>(userOnlineList, (long) onlineVOList.size());
    }

    @Override
    public void kickOutUser(String token) {
        StpUtil.logoutByTokenValue(token);
    }

}
