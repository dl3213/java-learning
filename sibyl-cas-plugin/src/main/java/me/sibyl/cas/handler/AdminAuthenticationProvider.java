//package me.sibyl.cas.handler;
//
//import com.alibaba.fastjson.JSONObject;
//import me.sibyl.cache.service.RedisService;
//import me.sibyl.cas.config.MyPasswordEncoder;
//import me.sibyl.cas.domain.SecurityUser;
//import me.sibyl.util.JwtUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Component;
//
///**
// * @Classname AdminAuthenticationProvider
// * @Description TODO
// * @Author dyingleaf3213
// * @Create 2023/02/26 00:13
// */
//@Component
//public class AdminAuthenticationProvider implements AuthenticationProvider {
//
//    private final UserDetailService userDetailsService;
//
//    private RedisService redisService;
//
//    @Autowired
//    public AdminAuthenticationProvider(UserDetailService userDetailsService, RedisService redisService) {
//        this.userDetailsService = userDetailsService;
//        this.redisService = redisService;
//    }
//
//    @Override
//    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
//        // 获取前端表单中输入后返回的用户名、密码
//        String userName = (String) authentication.getPrincipal();
//        String password = (String) authentication.getCredentials();
//
//        // 通过用户名称获取用户信息 (UserDetailsServiceImpl 这个类是实现了 Security 中提供的 UserDetailsService 用于处理用户信息 具体内容在下方)
//        SecurityUser securityUser = (SecurityUser) userDetailsService.loadUserByUsername(userName);
//
//        // 判断用户是否存在
//        if (securityUser == null) {
//            throw new UsernameNotFoundException("当前用户名不存在");
//        }
//        // 对可能出现的异常进行抛出，抛出的一次会跳到登录失败中进行集中处理
//        if (securityUser.isEnabled()) {
//            throw new DisabledException("该账户已被禁用，请联系管理员");
//        } else if (securityUser.isAccountNonLocked()) {
//            throw new LockedException("该账户已被锁定");
//        } else if (securityUser.isAccountNonExpired()) {
//            throw new AccountExpiredException("该账户已过期，请联系管理员");
//        } else if (securityUser.isCredentialsNonExpired()) {
//            throw new CredentialsExpiredException("该账户的登陆凭证已过期，请重新登录");
//        }
//
//        // 密码验证，用于验证传进来的密码和加密后的密码是否是同一个。
//        boolean isValid = MyPasswordEncoder.matches(password, securityUser.getPassword());
//        if (!isValid) {
//            //抛出密码异常
//            throw new BadCredentialsException("密码错误！");
//        }
//
//        // 前后端分离情况下 处理逻辑...
//        // 更新登录令牌 - 之后访问系统其它接口直接通过token认证用户权限...
//        String id = securityUser.getCurrentUserInfo().getId();
//        String jwt = JwtUtil.createJwt(
//                id,
//                securityUser.getCurrentUserInfo().getUsername(),
//                JSONObject.parseObject(JSONObject.toJSONString(securityUser.getCurrentUserInfo()))
//        );
//
//        redisService.set("login:" + id, securityUser);
//        securityUser.setToken(jwt);
//        // 通过 redis 缓存数据
//        redisService.set(jwt, securityUser);
//
//        return new UsernamePasswordAuthenticationToken(securityUser, password, securityUser.getAuthorities());
//    }
//
//    @Override
//    public boolean supports(Class<?> aClass) {
//        //确保 aClass 能转成该类
//        return aClass.equals(UsernamePasswordAuthenticationToken.class);
//    }
//}
