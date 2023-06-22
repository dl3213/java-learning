package me.sibyl.cas.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.cas.util.WebUtil;
import me.sibyl.common.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname AdminAuthenticationEntryPoint
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/26 00:18
 */

@Slf4j
@Component
public class AdminAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String message = e.getMessage();
        Object result;
        if(e instanceof AccountExpiredException){
            // 判断错误类型并且返回数据 主要处理的是Token过期问题
            // 其中HttpStatus类是自己定义的一个枚举类。用于定义状态码和消息
            result = Response.error(HttpStatus.UNAUTHORIZED.value(),message);
        }else{
            result = Response.error(HttpStatus.UNAUTHORIZED.value(), "token失效");
        }
        WebUtil.renderString(httpServletResponse, JSON.toJSONString(result));
    }
}
