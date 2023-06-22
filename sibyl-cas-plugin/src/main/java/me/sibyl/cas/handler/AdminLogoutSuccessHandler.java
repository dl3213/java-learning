package me.sibyl.cas.handler;

import com.alibaba.fastjson.JSON;
import me.sibyl.cas.util.WebUtil;
import me.sibyl.common.response.Response;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname AdminLogoutSuccessHandler
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/26 00:08
 */

@Component
public class AdminLogoutSuccessHandler implements LogoutSuccessHandler {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        String token = httpServletRequest.getHeader("token");
        redisTemplate.delete(token);
        Object result = Response.success("退出成功");
        WebUtil.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
