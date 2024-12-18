package code.sibyl.aop

import code.sibyl.common.r
import code.sibyl.domain.ActionLog
import com.google.gson.Gson
import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import java.util.*


@Aspect
@Component
@Slf4j
class ActionLogAop {

    private val log = LoggerFactory.getLogger(ActionLogAop::class.java)

    @Value("\${app.key}")
    private lateinit var appKey: String;

    @Pointcut("@annotation(actionLog)")
    fun pointCut(actionLog: code.sibyl.aop.ActionLog?) {
    }

    @Around("pointCut(actionLog)") //??? @Around才能获取 到 ReactiveSecurityContextHolder.getContext()
    @Throws(Throwable::class)
    fun afterReturning(joinPoint: ProceedingJoinPoint, actionLog: code.sibyl.aop.ActionLog): Any {
        var class_ = joinPoint.target?.javaClass?.name
        var method = joinPoint.signature?.name
        var proceed = joinPoint.proceed()

        if ((proceed !is Mono<*>) && (proceed !is Flux<*>)) {
            return proceed;
        }

        var mono: Mono<out Any>? = null;
        if (proceed is Mono<*>) {
            mono = proceed;
        }
        if (proceed is Flux<*>) {
            var flux = (proceed as Flux<out Any>); // todo 只适用于有限流  todo 无限流无法获取结果     todo doFinally 无法获取contex
            mono = flux.collectList();
        }

        //todo 针对void类型的返回

        var logBuilder = Mono.zip(
            mono as Mono<out Any>,
            Mono.just(joinPoint),
            Mono.just(actionLog),
            r.getWebExchange().switchIfEmpty(Mono.just(EmptyServerWebExchange())),
//            ReactiveSecurityContextHolder.getContext().switchIfEmpty(Mono.just(SecurityContextImpl()))
        )
            .flatMap { t ->
                var funRet = t.t1

                return@flatMap Mono.zip(Mono.just(funRet), Mono.just(1L))
            }

        if (proceed is Mono<*>) {
            return logBuilder.flatMap { t ->
                return@flatMap Mono.just(t.t1)
            }
        }
        if (proceed is Flux<*>) {
            return logBuilder.flatMapMany { t ->
                return@flatMapMany Flux.fromIterable(t.t1 as MutableList<out Any>);
            }
        }

        return proceed;
    }

}
