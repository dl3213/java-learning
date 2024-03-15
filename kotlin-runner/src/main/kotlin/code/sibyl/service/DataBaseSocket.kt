package code.sibyl.service

import org.antlr.v4.runtime.misc.MultiMap
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.CloseStatus
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Flux
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap


@Component
class DataBaseSocket : WebSocketHandler {

    private val log = LoggerFactory.getLogger(DataBaseSocket::class.java)
    private val clientMap: Map<WebSocketSession, FluxSink<WebSocketMessage>> = ConcurrentHashMap();

    override fun handle(session: WebSocketSession): Mono<Void> {
        log.info("socket get")
        val handshakeInfo = session.handshakeInfo
//        if (handshakeInfo.uri.query == null) {
//            return session.close(CloseStatus.REQUIRED_EXTENSION)
//        } else {
//            val isValidate: Boolean = true;
//            if (!isValidate) {
//                return session.close()
//            }
//        }
        val output = session.receive()
            .concatMap { mapper ->
                val msg = mapper.payloadAsText
                log.info("mapper: $msg")
                Flux.just(msg)
            }.map { value ->
                log.info("value: $value")
                session.textMessage("Echo $value")
            }
        return session.send(output)

    }
}