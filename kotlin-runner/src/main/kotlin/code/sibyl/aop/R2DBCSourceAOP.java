package code.sibyl.aop;

import code.sibyl.config.R2dbdRoutingConfig;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Method;

@Component
@Aspect
@Order(Ordered.HIGHEST_PRECEDENCE)
public class R2DBCSourceAOP {

    @Pointcut(value = "(@annotation(code.sibyl.aop.DS) || @within(code.sibyl.aop.DS))")
    public void point() {
    }

    @Around(value = "point()")
    public Object dynamicSelectSource(ProceedingJoinPoint pjp) throws Throwable {
        //System.err.println("dynamicSelectSource");
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        DS r2DBCSource = method.getAnnotation(DS.class);
        if (r2DBCSource == null) {
            r2DBCSource = method.getDeclaringClass().getAnnotation(DS.class);
        }
        String value = r2DBCSource.value();
        if (method.getReturnType() == Mono.class) {
            return R2dbdRoutingConfig.putR2dbcSource((Mono<?>) pjp.proceed(), value);
        } else if (method.getReturnType() == Flux.class) {
            return R2dbdRoutingConfig.putR2dbcSource((Flux<?>) pjp.proceed(), value);
        } else {
            //throw new RuntimeException("不支持别的发布类型");
            return pjp.proceed();
        }
        //return pjp.proceed();
    }
}
