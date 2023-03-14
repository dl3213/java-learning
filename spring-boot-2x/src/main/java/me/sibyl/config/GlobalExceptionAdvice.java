package me.sibyl.config;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.config.SibylException;
import me.sibyl.common.domain.CommonEnum;
import me.sibyl.common.response.Response;
import me.sibyl.common.response.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Classname GlobalExceptionAdvice
 * @Description 全局异常处理
 * @Author dyingleaf3213
 * @Create 2022/05/04 23:07
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionAdvice {

    /**
     * 处理自定义的业务异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = SibylException.class)
    @ResponseBody
    public  Response bizExceptionHandler(HttpServletRequest req, SibylException e){
        log.error("发生业务异常！原因是：{}",e.getMsg());
        return Response.error(e.getCode(),e.getMsg());
    }

    /**
     * 处理空指针的异常
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value =NullPointerException.class)
    @ResponseBody
    public Response exceptionHandler(HttpServletRequest req, NullPointerException e){
        log.error("发生空指针异常！原因是:",e);
        return Response.error(CommonEnum.NOT_FOUND);
    }


    /**
     * 处理其他异常
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Response exceptionHandler(Exception e){
        log.error("未知异常！原因是:{}", e);
        return Response.error(500, e.getMessage());
    }
}
