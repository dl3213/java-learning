package me.sibyl.cas.handler;//package me.sibyl.cas.handler;
//
//import com.alibaba.fastjson.JSON;
//import me.sibyl.common.domain.CommonEnum;
//import me.sibyl.common.response.Response;
//import me.sibyl.util.web.WebUtil;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.AuthenticationEntryPoint;
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
//public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
//        Response error = Response.error(CommonEnum.SIGNATURE_NOT_MATCH);
//        WebUtil.renderString(response, JSON.toJSONString(error));
//    }
//}
