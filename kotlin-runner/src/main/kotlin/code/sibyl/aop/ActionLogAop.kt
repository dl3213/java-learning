package code.sibyl.aop

import code.sibyl.common.r
import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuples


@Aspect
@Component
@Slf4j
class EosActionLogAop {

    private val log = LoggerFactory.getLogger(EosActionLogAop::class.java)


    @Pointcut("@annotation(actionLog)")
    fun pointCut(actionLog: ActionLog?) {
    }

    @Around("pointCut(actionLog)") //??? @Around才能获取 到 ReactiveSecurityContextHolder.getContext()
    @Throws(Throwable::class)
    fun afterReturning(joinPoint: ProceedingJoinPoint, actionLog: ActionLog): Any {

        var class_ = joinPoint.target?.javaClass?.name
        var method = joinPoint.signature?.name
        var proceed = joinPoint.proceed()

        if (r.isReactive((joinPoint.signature as MethodSignature).method)) {

            var ret: Any? = null
            if (proceed is Mono<*>) {
                println("proceed is Mono<*>")
                var mono = proceed as Mono<out Any>;
                return Mono.zip(mono, r.getWebExchange()).flatMap { t ->
                    println("Mono.zip")
                    println(t.t1)
                    println(t.t2.request?.uri?.toString())
                    return@flatMap Mono.just(t.t1)
                }
            }
            if (proceed is Flux<*>) {
                println("proceed is Flux<*>")
                proceed.count().subscribe { e -> println("flux size => $e") }
                var flux = (proceed as Flux<out Any>); // todo 无限流测试

                return flux.transformDeferredContextual { e, c ->
                    println("transformDeferredContextual")
                    println(c.get(ServerWebExchange::class.java))
//                    (e, c.get(ServerWebExchange::class.java))
                    e
//                    return@transformDeferredContextual Flux.zip(
//                        Flux.just(e),
//                        Flux.just(c.get(ServerWebExchange::class.java))
//                    )
                }
                    .map { t ->
                        println("Flux.zip ")
//                        println(t.t1)
//                        println(t.t2.request?.uri?.toString())
                        t
                        //(t.t1 as MutableIterable<Any>?)
                    }
                    .doFinally { e ->
                        println("doFinally")
                        println(e.name)
                        println(e.javaClass)
                        println(e.ordinal)
                    }

//                return Mono.zip(flux.collectList(), r.getWebExchange())  // todo 只适用与有限流
//                    .flatMapMany { t ->
//                        println("Mono.zip flux ")
//                        println(t.t1)
//                        println(t.t2.request?.uri?.toString())
//                        return@flatMapMany Flux.fromIterable(t.t1)
//                    }

//                Flux.from(flux).contextCapture().map { e ->
//                    println("contextCapture().map")
//                    println(e)
//                    e
//                }.subscribe()
//
//                Flux.zip(flux, r.getWebExchange_flux()).map { t ->
//                    println("Flux.zip")
//                    println(t.t1)
//                    println(t.t2)
//                }.subscribe()

//                Flux.from(flux).contextCapture().transformDeferredContextual { e, context ->
//                    println("transformDeferredContextual")
//                    println(context.hasKey(ServerWebExchange::class.java))
////                println(context.get(ServerWebExchange::class.java).request.uri.toString())
//                    return@transformDeferredContextual e;
//                }.flatMap { t ->
//                    println("just flatMap")
//                    println(t)
//                    Mono.zip(
//                        Mono.just(t),
//                        Mono.just(joinPoint),
//                        Mono.just(actionLog),
////                    r.getWebExchange()
//                    )
//                }.map { t ->
//                    println("just map")
//                    println(t.t1)
//                    println(t.t2)
//                    t
//                }.subscribe()


            }
            //if (ret == null) throw RuntimeException(class_ + "." + method + "该方法无法推断响应式返回类型");

//            Flux.deferContextual { data: ContextView? ->
//                Mono.just(
//                    data as ContextView
//                )
//            }.cast(
//                Context::class.java
//            ).filter { context: Context? ->
//                context?.hasKey(ServerWebExchange::class.java) == true
//            }.map { context: Context? ->
//                context?.get(ServerWebExchange::class.java)
//            }.map { it ->
//                println("WebExchange => " + it)
//                println(it?.let { it1 -> r.eosUser(it1) })
//            }

//            Mono.zip(Mono.just(proceed), r.getWebExchange()).map { e ->
//                println("WebExchange => " + e.t2)
//                println(r.eosUser(e.t2))
//            }
//

//            ret.

//            ret.transformDeferredContextual { e, context ->
//                println("transformDeferredContextual")
//                println(context.hasKey(ServerWebExchange::class.java))
////                println(context.get(ServerWebExchange::class.java).request.uri.toString())
//                return@transformDeferredContextual e;
//            }.flatMap { t ->
//                println("just flatMap")
//                println(t)
//                Mono.zip(
//                    Mono.just(t),
//                    Mono.just(joinPoint),
//                    Mono.just(actionLog),
////                    r.getWebExchange()
//                )
//            }.map { t ->
//                println("just map")
//                println(t.t1)
//                println(t.t2)
//                t
//            }
//                .subscribe()
//
//            return Mono.zip(
//                ret, // 直接执行获取结果
//                Mono.just(joinPoint),
//                Mono.just(actionLog),
////                r.getWebExchange_flux().collectList()
//            ).flatMap { t ->
//
//                println(t.t3.topic)
//                println(t.t2.target?.javaClass?.name)
//                println(t.t2.signature?.name)
////                println(t.t4.first()?.request?.uri?.toString())
//
//                return@flatMap Mono.zip(Mono.just(t.t1), Mono.just(1))
//                //return@flatMap t.t1 as Mono<out Any>?
//            }.flatMap { t ->
//                return@flatMap Mono.just(t.t1)
//            }

            return proceed;


        } else {
            // todo 非反应式无法获取 ReactiveSecurityContextHolder.getContext() 和 ServerWebExchange
            return proceed;
        }
    }

}
