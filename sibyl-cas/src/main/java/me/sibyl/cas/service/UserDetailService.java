package me.sibyl.cas.service;

import com.alibaba.fastjson.JSONObject;
import me.sibyl.cache.service.RedisService;
import me.sibyl.cas.domain.*;
import me.sibyl.cas.mapper.*;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.util.JwtUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

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
    private RedisTemplate redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userMapper.queryByUsername(username);

        Assert.notNull(user,"查询用户不存在");
        //根据用户ID获取角色权限信息
        List<Role> userRoles = userMapper.queryUserRoles(user.getId());
        List<Permission> userRolePermissions = userMapper.queryUserRolePermissions(user.getId());
        // 返回 SecurityUser 这个类上面有介绍
        SecurityUser securityUser = new SecurityUser(user, userRoles, userRolePermissions);
        String jwt = JwtUtil.createJwt(user.getId(), securityUser.getUsername(), JSONObject.parseObject(JSONObject.toJSONString(securityUser.getCurrentUserInfo())));
        log.info("[login]token=" + jwt);
        redisTemplate.opsForValue().set(jwt, securityUser);
        securityUser.setToken(jwt);
        return securityUser;
    }
}
