package me.sibyl.auth;

import com.alibaba.fastjson2.JSON;
import me.sibyl.common.response.Response;
import me.sibyl.util.web.WebUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {
        WebUtil.renderString(response, JSON.toJSONString(Result.unauth("权限不足")));
    }
}
