//package me.sibly.microservice.application.aspect;
//
//
//import lombok.extern.slf4j.Slf4j;
//import me.sibly.microservice.application.annotation.RequestCountLimit;
//import me.sibly.microservice.application.util.RequestUtils;
//import me.sibyl.microservice.common.response.ResponseVO;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import java.util.Objects;
//import java.util.concurrent.TimeUnit;
//
//
///**
// * @Classname RequestCountLimitAspect
// * @Description 时间段内限制请求次数
// * @Date 2022/3/29 14:14
// * @Author by Qin Yazhi
// */
//
//@Aspect
//@Component
//@Slf4j
////@Order(11)
//public class RequestCountLimitAspect {
//
//    public static final String keyPrefix = "requestCountLimit-";
//
//    @Resource
//    private RedisTemplate redisTemplate;
//
//    @Pointcut("@annotation(requestCountLimit)")
//    public void pointCut(RequestCountLimit requestCountLimit) {
//    }
//
//    @Around("pointCut(requestCountLimit)")
//    public Object doAround(ProceedingJoinPoint pjp, RequestCountLimit requestCountLimit) throws Throwable {
//        ValueOperations<String, Integer> opsForValue = redisTemplate.opsForValue();
//
//        try {
//            // 获得request对象
//            HttpServletRequest request = RequestUtils.getRequest();
//            String sessionId = RequestUtils.getServletRequestAttributes().getSessionId();
//
//            String requestCountLimitKey = keyPrefix + sessionId + "-" + request.getServletPath();
//            //String noRepeatSubmitKey = NoRepeatSubmitAspect.keyPrefix +  sessionId + "-" + request.getServletPath();
//
//            // 从缓存中获取请求次数
//            Integer count = opsForValue.get(requestCountLimitKey);
//            log.info("[限制请求次数切片处理]redis-key: {} , value: {}", requestCountLimitKey, count);
//
//            //null -> 第一次请求
//            if(Objects.isNull(count)) {
//                opsForValue.set(requestCountLimitKey, requestCountLimit.count() - 1, requestCountLimit.time(), TimeUnit.SECONDS);
//            }else {
//                // 0 -> 次数限制
//                if(Objects.nonNull(count) && count == 0){
//                    return new ResponseVO(800200,"接口请求超过次数");
//                }else {// -1 保存
//                    // SETRANGE?
//                    opsForValue.increment (requestCountLimitKey, - 1 );
//                }
//            }
//
//            Object result = pjp.proceed();
//            return result;
//        }catch (Exception e){
//            e.printStackTrace();
//            log.error("[限制请求次数切片处理]验证限制请求次数时出现未知异常");
//            return new ResponseVO(800500,"验证限制请求次数时出现未知异常");
//        }
//    }
//
//}