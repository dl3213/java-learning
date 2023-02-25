package me.sibyl.cas.aop;

import me.sibyl.common.response.Response;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname GlobalExceptionHandler
 * @Description GlobalExceptionHandler
 * @Date 2022/5/24 13:27
 * @Author by Qin Yazhi
 */
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@RestControllerAdvice(value = {"me.sibyl.cas.controller"})
//public class BaseGlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public Response bindException(HttpServletRequest request, Exception e, BindingResult bindingResult) {
//        e.printStackTrace();
//        return Response.error(bindingResult.getFieldError().getDefaultMessage());
//    }
//}
