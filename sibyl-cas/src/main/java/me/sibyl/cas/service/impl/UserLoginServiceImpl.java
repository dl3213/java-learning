package me.sibyl.cas.service.impl;

import com.alibaba.fastjson.JSONObject;
import me.sibyl.base.domain.ResponseVO;
import me.sibyl.base.entity.User;
import me.sibyl.cache.service.RedisService;
import me.sibyl.cas.domain.LoginUser;
import me.sibyl.cas.service.UserLoginService;
import me.sibyl.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Resource;

/**
 * @Classname UserLoginServiceImpl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 11:27
 */
@Service
public class UserLoginServiceImpl implements UserLoginService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private RedisService redisService;

    @Override
    public ResponseVO login(User user) {

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword());

        Authentication authenticate = authenticationManager.authenticate(token);

        Assert.notNull(authenticate,"登录失败");

        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        String uid = loginUser.getUser().getUsername();

        String jwt = JwtUtil.createJwt(uid, loginUser.getUser().getUsername(), JSONObject.parseObject(JSONObject.toJSONString(loginUser.getUser())));

        redisService.set("login:"+uid, loginUser);

        return ResponseVO.success(200,"登录成功",jwt);
    }

    @Override
    public ResponseVO logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        LoginUser loginUser = (LoginUser) principal;
        String username = loginUser.getUser().getUsername();
        redisService.delete("login:"+username);

        return ResponseVO.success("注销成功");
    }
}
