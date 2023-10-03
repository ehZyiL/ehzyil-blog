package com.ehzyil.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ehzyil.domain.User;
import com.ehzyil.mapper.MenuMapper;
import com.ehzyil.mapper.RoleMapper;
import com.ehzyil.mapper.UserMapper;
import com.ehzyil.model.dto.EmailDTO;
import com.ehzyil.model.dto.UserInfoDTO;
import com.ehzyil.model.dto.UserPasswordDTO;
import com.ehzyil.model.vo.admin.MetaVO;
import com.ehzyil.model.vo.admin.RouterVO;
import com.ehzyil.model.vo.admin.UserBackInfoVO;
import com.ehzyil.model.vo.admin.UserMenuVO;
import com.ehzyil.model.vo.front.UserInfoVO;
import com.ehzyil.service.IUserService;
import com.ehzyil.service.RedisService;
import com.ehzyil.strategy.context.UploadStrategyContext;
import com.ehzyil.utils.SecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.ehzyil.constant.CommonConstant.*;
import static com.ehzyil.constant.RedisConstant.*;
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

    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private MenuMapper menuMapper;


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
    public String updateAvatar(MultipartFile multipartFile) {
        //头像上传
        String avatar = uploadStrategyContext.executeUploadStrategy(multipartFile, AVATAR.getPath());

        //更新用户头像
        User user = User.builder()
                .id(StpUtil.getLoginIdAsInt())
                .avatar(avatar)
                .build();
        getBaseMapper().updateById(user);
        return avatar;
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


}
