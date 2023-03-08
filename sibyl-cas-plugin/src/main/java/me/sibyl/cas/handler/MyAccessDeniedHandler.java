package me.sibyl.cas.handler;

import com.alibaba.fastjson.JSON;
import me.sibyl.common.response.Response;
import me.sibyl.util.WebUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Classname MyAccessDeniedHandler
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/26 00:20
 */

@Component
public class MyAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        e.printStackTrace();
        Response error = Response.error(403, e.getMessage());
        WebUtil.renderString(httpServletResponse, JSON.toJSONString(error));
    }
}

