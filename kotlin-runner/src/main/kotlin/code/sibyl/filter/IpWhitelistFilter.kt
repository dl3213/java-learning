package code.sibyl.filter

import code.sibyl.common.Response
import com.alibaba.fastjson2.JSONObject
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets


@Configuration
@Order(-1)
class IpWhitelistFilter : WebFilter {

    @Value("\${ip-whitelist.enabled:true}")
    private var enabled: Boolean = true

    @Value("\${ip-whitelist.ips:127.0.0.1,::1,0:0:0:0:0:0:0:1}")
    private lateinit var whitelistIps: String

    private val whitelist: MutableSet<String> = HashSet()

    private val log = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun init() {
        whitelist.clear()
        whitelistIps.split(",").forEach { ip ->
            whitelist.add(ip.trim())
        }
        log.info("IP Whitelist Filter initialized. enabled={}, whitelist={}", enabled, whitelist)
    }

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        if (!enabled) {
            return chain.filter(exchange)
        }

        val path = exchange.request.path.value()

        // 仅拦截 /api/rest/** 路径
        if (!path.startsWith("/api/rest/")) {
            return chain.filter(exchange)
        }

        // 放行 OPTIONS 预检请求（CORS）
        if (exchange.request.method.name() == "OPTIONS") {
            return chain.filter(exchange)
        }

        val clientIp = getClientIp(exchange.request)

        if (clientIp == null || !whitelist.contains(clientIp)) {
            log.warn("Blocked request from IP: {}, path: {}", clientIp, path)
            val response = Response.error(HttpStatus.FORBIDDEN.value(), "IP not allowed: $clientIp")
            exchange.response.headers.contentType = MediaType.APPLICATION_JSON
            return exchange.response.writeWith(
                Mono.just(
                    exchange.response.bufferFactory().wrap(
                        JSONObject.toJSONString(response).toByteArray(StandardCharsets.UTF_8)
                    )
                )
            )
        }

        return chain.filter(exchange)
    }

    /**
     * 获取客户端真实 IP
     * 优先级: X-Forwarded-For > X-Real-IP > remoteAddress
     */
    private fun getClientIp(request: ServerHttpRequest): String? {
        // 代理环境: X-Forwarded-For
        val forwardedFor = request.headers.getFirst("X-Forwarded-For")
        if (!forwardedFor.isNullOrBlank()) {
            return forwardedFor.split(",").first().trim()
        }

        // Nginx 代理: X-Real-IP
        val realIp = request.headers.getFirst("X-Real-IP")
        if (!realIp.isNullOrBlank()) {
            return realIp.trim()
        }

        // 直连: remoteAddress
        return request.remoteAddress?.address?.hostAddress
    }
}
