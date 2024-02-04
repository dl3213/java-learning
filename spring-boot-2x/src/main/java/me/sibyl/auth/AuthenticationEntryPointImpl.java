package me.sibyl.auth;

import com.alibaba.fastjson2.JSON;
import me.sibyl.common.response.Response;
import me.sibyl.util.web.WebUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest equest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        WebUtil.renderString(response, JSON.toJSONString(Result.auth("token认证失败")));
    }
}
