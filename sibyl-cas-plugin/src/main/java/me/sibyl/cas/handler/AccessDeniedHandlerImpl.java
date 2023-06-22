package me.sibyl.cas.handler;//package me.sibyl.cas.handler;
//
//import com.alibaba.fastjson.JSON;
//import me.sibyl.common.domain.CommonEnum;
//import me.sibyl.common.response.Response;
//import me.sibyl.util.web.WebUtil;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
///**
// * @Classname AuthenticationEntryPointImpl
// * @Description TODO
// * @Author dyingleaf3213
// * @Create 2022/02/26 16:04
// */
//@Component
//public class AccessDeniedHandlerImpl implements AccessDeniedHandler {
//
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) {
//        Response error = Response.error(CommonEnum.SIGNATURE_NOT_MATCH);
//        WebUtil.renderString(response, JSON.toJSONString(error));
//    }
//}
