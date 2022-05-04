package me.sibyl.cas.filter;

import me.sibyl.cas.base.config.SibylException;
import me.sibyl.cas.domain.LoginUser;
import me.sibyl.cas.util.JwtUtil;
import me.sibyl.cas.util.RedisUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname JwtAuthTokenFilter
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 13:22
 */
@Component
public class JwtAuthTokenFilter extends OncePerRequestFilter {

    @Resource
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if(!StringUtils.hasText(token)){
            //放行
            filterChain.doFilter(request,response);
            return;
        }
        String subject;
        try {
            Claims claims = JwtUtil.parseJwt(token);
            subject = claims.getSubject();
        }catch (Exception e){
            e.printStackTrace();
            throw new SibylException("token非法");
        }

        String redisKey = "login:"+ subject;
        LoginUser loginUser = redisUtil.get(redisKey);

        Assert.notNull(loginUser,"用户未登录");

        //TODO 获取权限封装到Authentication
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        //
        filterChain.doFilter(request,response);
    }
}
