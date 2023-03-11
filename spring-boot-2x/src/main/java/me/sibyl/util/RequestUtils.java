package me.sibyl.util;


import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestUtils {

    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) getServletRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    public static HttpServletRequest request(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) getServletRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        return request;
    }

    public static RequestAttributes getServletRequestAttributes(){
        return RequestContextHolder.getRequestAttributes();
    }

    public static HttpServletResponse response(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) getServletRequestAttributes();
        HttpServletResponse response = attributes.getResponse();
        return response;
    }
}
