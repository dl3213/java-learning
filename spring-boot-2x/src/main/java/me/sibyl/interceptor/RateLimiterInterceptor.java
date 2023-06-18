package me.sibyl.interceptor;

import com.alibaba.fastjson2.JSON;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;

/**
 * @author dyingleaf3213
 * @Classname RateLimiterInterceptor
 * @Description TODO
 * @Create 2023/06/18 19:51
 */
@Component
@Slf4j
public class RateLimiterInterceptor implements HandlerInterceptor {

    private RateLimiter rateLimiter;

    @PostConstruct
    public void init() {
        this.rateLimiter = RateLimiter.create(1);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(this.rateLimiter.tryAcquire()) {
            //成功获取到令牌
            return true;
        }
        //获取失败，直接响应“错误信息”
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(JSON.toJSONString(Response.error(400, "服务器繁忙，请稍后重试！")));
        return false;
    }
}
