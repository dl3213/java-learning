package me.sibyl.cas.handler;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.scripts.JS;
import me.sibyl.cas.domain.SecurityUser;
import me.sibyl.common.response.Response;
import me.sibyl.util.WebUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @Classname AdminAuthenticationSuccessHandler
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/25 23:59
 */

@Component
public class AdminAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        // SecurityUser 是Spring Security里面UserDetailsd的实现类里面包含了用户信息
        SecurityUser securityUser = ((SecurityUser) authentication.getPrincipal());
        // 从SecurityUser 中取出Token
        String token = securityUser.getToken();
        // 定义一个map集合 用于返回JSON数据
        //HashMap<String, String> map = new HashMap<String, String>(1);
        // 前端会收到 {"token":"token值"}
        //map.put("token",token);
        // 结果集封装 规范返回内容
        Object result = Response.success("操作成功", token);
        // 将封装数据进行返回。 这里有一个返回工具类
        WebUtil.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}

