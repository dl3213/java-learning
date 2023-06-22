package me.sibyl.cas.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.cas.util.WebUtil;
import me.sibyl.common.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname AdminAuthenticationFailureHandler
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/25 23:49
 */

@Slf4j
@Component
public class AdminAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        Object result;
        // 对异常信息进行封装处理返回给前端
        e.printStackTrace();
        if (e instanceof UsernameNotFoundException || e instanceof BadCredentialsException) {
            result = Response.error(HttpStatus.PAYMENT_REQUIRED.value(),e.getMessage());
        } else if (e instanceof LockedException) {
            result = Response.error(HttpStatus.PAYMENT_REQUIRED.value(),"账户被锁定，请联系管理员!");
        } else if (e instanceof CredentialsExpiredException) {
            result = Response.error(HttpStatus.PAYMENT_REQUIRED.value(),"证书过期，请联系管理员!");
        } else if (e instanceof AccountExpiredException) {
            result = Response.error(HttpStatus.PAYMENT_REQUIRED.value(),"账户过期，请联系管理员!");
        } else if (e instanceof DisabledException) {
            result = Response.error(HttpStatus.PAYMENT_REQUIRED.value(),"账户被禁用，请联系管理员!");
        } else {
            log.error("登录失败：", e);
            result = Response.error(HttpStatus.PAYMENT_REQUIRED.value(),"登录失败!");
        }
        WebUtil.renderString(response, JSON.toJSONString(result));
    }
}
