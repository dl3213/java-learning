package me.sibyl.cas.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.sibyl.base.entity.*;
import me.sibyl.cache.service.RedisService;
import me.sibyl.cas.domain.SecurityUser;
import me.sibyl.cas.mapper.*;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.util.JwtUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Classname UserDetailService
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 10:37
 */
@Service
@Slf4j
public class UserDetailService implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;

    @Resource
    private RedisService redisService;

    public static final String login_cache_key = "sibyl-login-";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername,username);
        User user = userMapper.selectOne(queryWrapper);

        Assert.notNull(user,"查询用户不存在");
        List<Role> userRoles = getUserRoles(user.getId());
        List<Permission> userRolePermissions = getUserRolePermissions(userRoles);
        // 返回 SecurityUser 这个类上面有介绍
        SecurityUser securityUser = new SecurityUser(user, userRoles, userRolePermissions);
        String jwt = JwtUtil.createJwt(user.getId(), securityUser.getUsername(), JSONObject.parseObject(JSONObject.toJSONString(securityUser.getCurrentUserInfo())));
        log.info("[login]token=" + jwt);
        redisService.set(jwt, securityUser);
        securityUser.setToken(jwt);
        return securityUser;
    }

    /**
     * 根据用户ID获取角色权限信息
     *
     * @param userId
     * @return
     */
    private List<Role> getUserRoles(String userId) {
        //通过用户ID查询角色中间表
        //通过角色中间表查询角色列表
        // 返回角色列表

        List<UserRole> userRoles = userRoleMapper.selectList(
                Wrappers.lambdaQuery(new UserRole())
                        .eq(UserRole::getUserId, userId)
        );
        List<String> roleIdList = userRoles
                .stream()
                .filter(Objects::nonNull)
                .map(UserRole::getRoleId).collect(Collectors.toList());

        List<Role> collect = roleIdList.stream()
                .map(roleMapper::selectById)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        return collect;
    }

    /**
     * 通过角色列表获取权限信息字符串
     *
     * @return
     */
    private Set<String> getUserRolePermissionsAccessCode(List<Role> roleList) {
        /**
         * 通过角色权限中间表查询
         */
        return roleList
                .stream()
                .filter(Objects::nonNull)
                .map(Role::getId)
                .distinct()
                .flatMap(e ->
                        rolePermissionMapper.selectList(
                                Wrappers.lambdaQuery(new RolePermission())
                                        .eq(RolePermission::getRoleId, e)
                        ).stream()
                )
                .filter(Objects::nonNull)
                .map(RolePermission::getPId)
                .map(permissionMapper::selectById)
                .filter(Objects::nonNull)
                .map(Permission::getCode)
                .distinct()
                .collect(Collectors.toSet());
    }

    /**
     * 通过角色列表获取权限信息
     *
     * @return
     */
    private List<Permission> getUserRolePermissions(List<Role> roleList) {
        /**
         * 通过角色权限中间表查询
         */
        return roleList
                .stream()
                .filter(Objects::nonNull)
                .map(Role::getId)
                .distinct()
                .flatMap(e ->
                        rolePermissionMapper.selectList(
                                Wrappers.lambdaQuery(new RolePermission())
                                        .eq(RolePermission::getRoleId, e)
                        ).stream()
                )
                .filter(Objects::nonNull)
                .map(RolePermission::getPId)
                .map(permissionMapper::selectById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
