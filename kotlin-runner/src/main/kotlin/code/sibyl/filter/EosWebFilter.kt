package code.sibyl.filter

import code.sibyl.common.Response
import com.alibaba.fastjson2.JSONObject
import org.apache.commons.lang3.StringUtils
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

@Configuration
@Order(-1)
class EosWebFilter : WebFilter {

    private var key: String = "eos-token-key";

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        //println(exchange.request.path.toString())
        //println(exchange.request.headers.getFirst("eos-token-key"))

        //val objectMapper = ObjectMapper()

        return Mono.just(exchange.request.headers)
            .mapNotNull { headers ->  headers[key]?.get(0).orEmpty() }
            .flatMap { token ->
                if (StringUtils.isBlank(token)) {
                    //return@flatMap Mono.defer { chain.filter(exchange) }
                    return@flatMap exchange.response.writeWith(
                        Mono.just(
                            exchange.response.bufferFactory().wrap(
                                JSONObject.toJSONString(
                                    Response.error(
                                        HttpStatus.UNAUTHORIZED.value(),
                                        HttpStatus.UNAUTHORIZED.reasonPhrase
                                    )
                                ).toByteArray(
                                    StandardCharsets.UTF_8
                                )
                            )
                        )
                    );
                } else {
                    return@flatMap Mono.defer { chain.filter(exchange) }
                }
            }
        ;

//        return chain.filter(exchange);
        //Mono.empty<Any>().switchIfEmpty {  }

//        return exchange.response.writeWith(
//            Mono.just(
//                exchange.response.bufferFactory().wrap(
//                    JSONObject.toJSONString(
//                        Response.error(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.reasonPhrase)
//                    ).toByteArray(
//                        StandardCharsets.UTF_8
//                    )
//                )
//            )
//        );

//        return Mono.empty<Void>() //.just() { exchange.request.headers.getFirst("eos-token-key") }
//            .switchIfEmpty {  }
//            .thenEmpty { chain.filter(exchange) }
    }

}