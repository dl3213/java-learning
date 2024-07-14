package code.sibyl.aop

import code.sibyl.repository.ActionLogRepository
import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.util.context.Context
import reactor.util.context.ContextView


@Aspect
@Component
@Slf4j
class ActionLogAop {

    private val log = LoggerFactory.getLogger(ActionLogAop::class.java)

    @Autowired
    private lateinit var actionLogRepository : ActionLogRepository;

    @Pointcut("@annotation(actionLog)")
    fun pointCut(actionLog: ActionLog?) {
    }

    @Around("pointCut(actionLog)") //??? @Around才能获取 到 ReactiveSecurityContextHolder.getContext()
    @Throws(Throwable::class)
    fun afterReturning(joinPoint: ProceedingJoinPoint, actionLog: ActionLog): Mono<Object> {

        return Mono.zip(
            Mono.just(1),
            Mono.just(joinPoint),
            Mono.just(actionLog),
//            ReactiveSecurityContextHolder.getContext().switchIfEmpty(Mono.error(RuntimeException("无用户信息")))
            ReactiveSecurityContextHolder.getContext(),
            Mono.deferContextual { data: ContextView? ->
                Mono.just(
                    data as ContextView
                )
            }.cast(
                Context::class.java
            ).filter { context: Context? ->
                context?.hasKey(ServerWebExchange::class.java) == true
            }.map { context: Context? ->
               context?.get(ServerWebExchange::class.java)
            }
        ).map { t ->
            println(t.t2.target?.javaClass?.name)
            println(t.t2.signature?.name)
            println("ActionLog -> Around -> " + t.t3?.topic)
            println(t.t4.authentication)
            println(t.t5?.request?.uri  )
//            return@map joinPoint.proceed() as Object?
            return@map t.t1
        }
            .doOnError { err ->
                println("doOnError")
                err.printStackTrace()
            }.then().map { e -> joinPoint.proceed() as Object? }

        try {
//            println("afterReturning")
//            println(Thread.currentThread().name)
//            println("ActionLog -> afterReturning -> " + actionLog?.topic)
////            println(userDetails?)
//            println(joinPoint?.target?.javaClass?.name)
//            println(joinPoint?.signature?.name)
//            println()
//            for (arg in joinPoint?.args!!) {
//                println(arg)
//            }
//            //println(r.getBean(ServerHttpSecurity::class.java).)

//            println(SecurityContextHolder.getContext())
//            println(SecurityContextHolder.getContext().authentication)

//            println(r.getBean(SessionRegistry::class.java))

//            Mono.deferContextual { context ->
//                context.toMono()
//            }.subscribe { context ->
//                println("Mono.deferContextual")
//                println(context)
//                println(context.hasKey(SecurityContext::class.java))
//                if(context.hasKey(SecurityContext::class.java)){
//                    println(context.get(SecurityContext::class.java).authentication)
//                }else{
//                    println("没有SecurityContext")
//                }
//
//            }

//            Mono.deferContextual { context -> context }.map { ctx -> ctx.get(CONTEXT_KEY) }

//            ReactiveSecurityContextHolder.getContext()
//                .map { e ->
//                println(e)
//                1;
//            }
//                .subscribe { context ->
//                println("ReactiveSecurityContextHolder.getContext()")
//                println(context)
////                println(context.authentication)
////                println(context.authentication.principal)
//            }


        } catch (e: Exception) {
            log.error("ActionLogAop.afterReturning => {}", e.message)
            e.printStackTrace()
        } finally {

        }
    }

}
