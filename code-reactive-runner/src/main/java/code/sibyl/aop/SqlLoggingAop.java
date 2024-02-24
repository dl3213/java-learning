//package code.sibyl.aop;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.context.ApplicationContext;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//
///**
// * @Classname SqlLoggingAop
// * @Description SqlLoggingAop
// * @Date 2023/4/19 15:57
// * @Author by Qin Yazhi
// */
//@Aspect
//@Component
//@Slf4j
//public class SqlLoggingAop {
//
//    /**
//     * 指定mapper
//     */
//    @Pointcut("execution(public * code.sibyl.controller..*.*(..))")
//    public void pointCut() {
//    }
//
//    @AfterReturning("pointCut()")
//    public void AfterReturning(JoinPoint joinPoint) throws Throwable {
//        System.err.println("AfterReturning");
//
//        Object[] args = joinPoint.getArgs();
//
//        for (Object arg : args) {
//            System.err.println("arg:"+arg.toString());
//            if(arg.getClass().getSimpleName().equals("ReactorServerHttpRequest")){
//                ServerHttpRequest request = (ServerHttpRequest)arg;
//                System.err.println("path => "+request.getURI().getPath());
//            }
//        }
//    }
//
//}
