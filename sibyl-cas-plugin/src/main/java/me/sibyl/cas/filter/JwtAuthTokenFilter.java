//package me.sibyl.cas.filter;
//
//import io.jsonwebtoken.Claims;
//import me.sibyl.cas.domain.SecurityUser;
//import me.sibyl.common.config.SibylException;
//import me.sibyl.cache.service.RedisService;
//import me.sibyl.util.JwtUtil;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.annotation.Resource;
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///**
// * @Classname JwtAuthTokenFilter
// * @Description TODO
// * @Author dyingleaf3213
// * @Create 2022/02/26 13:22
// */
//@Component
//public class JwtAuthTokenFilter extends OncePerRequestFilter {
//
//    @Resource
//    private RedisService redisService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String token = request.getHeader("token");
//        if(!StringUtils.hasText(token)){
//            //放行
//            filterChain.doFilter(request,response);
//            return;
//        }
//        String subject;
//        try {
//            Claims claims = JwtUtil.parseJwt(token);
//            subject = claims.getSubject();
//        }catch (Exception e){
//            e.printStackTrace();
//            throw new SibylException("token非法");
//        }
//
//        SecurityUser securityUser = redisService.get(token);
//
//        Assert.notNull(securityUser,"用户未登录");
//
//        //TODO 获取权限封装到Authentication
//        UsernamePasswordAuthenticationToken authenticationToken =
//                new UsernamePasswordAuthenticationToken(securityUser,null, securityUser.getAuthorities());
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//        //
//        filterChain.doFilter(request,response);
//    }
//}
