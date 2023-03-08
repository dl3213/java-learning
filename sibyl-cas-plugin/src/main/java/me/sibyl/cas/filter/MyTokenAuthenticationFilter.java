package me.sibyl.cas.filter;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.cache.service.RedisService;
import me.sibyl.cas.domain.SecurityUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @Classname MyTokenAuthenticationFilte
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/26 00:16
 */

@Slf4j
@Component
public class MyTokenAuthenticationFilter extends OncePerRequestFilter {

    private final RedisService redisService;

    public static final String login_cache_key = "sibyl-login-";

    public MyTokenAuthenticationFilter(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        log.info("请求头类型： {}" , httpServletRequest.getContentType());

//        MultiReadHttpServletRequest wrappedRequest = new MultiReadHttpServletRequest(httpServletRequest);
//        MultiReadHttpServletResponse wrappedResponse = new MultiReadHttpServletResponse(httpServletResponse);
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            // 记录请求的消息体
            //logRequestBody(wrappedRequest);

            // 前后端分离情况下，前端登录后将token储存在cookie中，每次访问接口时通过token去拿用户权限
            // Constants 这个类是自己定义的常量类REQUEST_HEADER这个表示token头的名称
            //String token = wrappedRequest.getHeader(Constants.REQUEST_HEADER);
            String token = httpServletRequest.getHeader("token");
            log.info("[MyTokenAuthenticationFilter]token=" + token);
            if (StringUtils.isNotBlank(token)) {
                // 检查token
                SecurityUser securityUser = (SecurityUser) redisService.get(token);
                if (securityUser == null || securityUser.getCurrentUserInfo() == null) {
                    throw new AccessDeniedException("TOKEN已过期，请重新登录！");
                }
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());

                // 全局注入角色权限信息和登录用户基本信息
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (AuthenticationException e) {
            log.error(e.getLocalizedMessage());
            // 清空当前线程中的 SecurityContext
            SecurityContextHolder.clearContext();
            // 执行 认证失败方法
            //this.adminAuthenticationEntryPoint.commence(wrappedRequest, wrappedResponse, e);
        } finally {
            stopWatch.stop();
            //long usedTimes = stopWatch.getTotalTimeMillis();
            // 打印消息
            //logResponseBody(wrappedRequest, wrappedResponse, usedTimes);
        }
    }

//    // 处理请求
//    private void logRequestBody(MultiReadHttpServletRequest request) {
//        MultiReadHttpServletRequest wrapper = request;
//        if (wrapper != null) {
//            try {
//                String bodyJson = wrapper.getBodyJsonStrByJson(request);
//                String url = wrapper.getRequestURI().replace("//", "/");
//                log.info("-------------------------------- 请求url: " + url + " --------------------------------");
//                Constants.URL_MAPPING_MAP.put(url, url);
//                log.info("`{}` 接收到的参数: {}",url , bodyJson);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    // 处理返回
//    private void logResponseBody(MultiReadHttpServletRequest request, MultiReadHttpServletResponse response, long useTime) {
//        MultiReadHttpServletResponse wrapper = response;
//        if (wrapper != null) {
//            byte[] buf = wrapper.getBody();
//            if (buf.length > 0) {
//                String payload;
//                try {
//                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
//                } catch (UnsupportedEncodingException ex) {
//                    payload = "[unknown]";
//                }
//                log.info("`{}`  耗时:{}ms  返回的参数: {}", Constants.URL_MAPPING_MAP.get(request.getRequestURI()), useTime, payload);
//                log.info("");
//            }
//        }
//    }

}

