package code.sibyl.aop

import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono


@Aspect
@Component
@Slf4j
class ActionLogAop {

    private val log = LoggerFactory.getLogger(ActionLogAop::class.java)

    private val context: Mono<SecurityContext> = ReactiveSecurityContextHolder.getContext()

    @Pointcut("@annotation(actionLog)")
    fun pointCut(actionLog: ActionLog?) {
    }

    @AfterReturning("pointCut(actionLog)")
    @Throws(Throwable::class)
    fun afterReturning(joinPoint: JoinPoint?, actionLog: ActionLog) {
        try {
//            println("afterReturning")
//            println("ActionLog -> afterReturning -> " + actionLog?.topic)
//            println(userDetails?)
//            println(joinPoint?.target?.javaClass?.name)
//            println(joinPoint?.signature?.name)
//            for (arg in joinPoint?.args!!) {
//                println(arg)
//            }
//            //println(r.getBean(ServerHttpSecurity::class.java).)
//            Mono.deferContextual { context ->
//                context.toMono()
//            }.subscribe { context ->
//                println("Mono.deferContextual")
//                println(context)
//            }
            ReactiveSecurityContextHolder.getContext().subscribe { context ->
                println("ReactiveSecurityContextHolder.getContext()")
                println(context.authentication)
                println(context.authentication.principal)
            }
        } catch (e: Exception) {
            log.error("ActionLogAop.afterReturning => {}", e.message)
            e.printStackTrace()
        } finally {

        }
    }
}
