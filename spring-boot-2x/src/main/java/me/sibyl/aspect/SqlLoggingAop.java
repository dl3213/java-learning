package me.sibyl.aspect;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.service.AsyncTaskService;
import me.sibyl.util.SqlUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @Classname SqlLoggingAop
 * @Description SqlLoggingAop
 * @Date 2023/4/19 15:57
 * @Author by Qin Yazhi
 */
@Aspect
@Component
@Slf4j
public class SqlLoggingAop {

//    @Pointcut("@annotation(sqlLogging)")
//    public void pointCut(SqlLogging sqlLogging) {
//    }

    /**
     * 指定mapper
     */
//    @Pointcut("execution(public * me.sibyl.dao.BaseMapper+.*(..))")
//    @Pointcut("execution(public * me.sibyl.dao..*.*(..))")
    public void pointCut() {
    }

    @Resource
    private SqlSessionFactory sqlSessionFactory;
    @Resource
    private AsyncTaskService asyncTaskService;

    @Resource
    private ApplicationContext applicationContext;


//    @AfterReturning("pointCut()")
    public void AfterReturning(JoinPoint joinPoint) throws Throwable {
        System.err.println(Thread.currentThread().getName() + " in aop");
        System.err.println("++++++++++AfterReturning开始+++++++++++");

        //1.从redis中获取主数据库，若获取不到直接退出，否则判断当前数据源是会否为主，若不为主，则切换到主数据源
        //2.调用目标方法
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        System.err.println("sra => " + sra);
        if (Objects.nonNull(sra) && Objects.nonNull(sra.getRequest())) {
            HttpServletRequest request = sra.getRequest();
            System.err.println("url: " + request.getRequestURI());
            System.err.println("method: " + request.getMethod());      //post or get? or ?
            System.err.println("class.method: " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
            System.err.println("args: " + joinPoint.getArgs());
        }

        //3.获取SQL
        String sql = SqlUtils.getMybatisSql(joinPoint, sqlSessionFactory);
        System.err.println(sql);
        System.err.println("++++++++++AfterReturning结束+++++++++++");
    }

}
