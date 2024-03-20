package code.sibyl.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketMessage
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.FluxSink
import reactor.core.publisher.Mono
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Supplier


@Component
class DataBaseSocket : WebSocketHandler {

    private val log = LoggerFactory.getLogger(DataBaseSocket::class.java)
    private val clientMap: Map<WebSocketSession, FluxSink<WebSocketMessage>> = ConcurrentHashMap();

    override fun handle(session: WebSocketSession): Mono<Void> {
        //clientMap.toMutableMap()[session] = session.receive();
        val handshakeInfo = session.handshakeInfo
        log.info("socket get => handshakeInfo.headers = {}", handshakeInfo.headers.get("Sec-Websocket-Key"))
        log.info("socket get => session = {}", session.id)

//        if (handshakeInfo.uri.query == null) {
//            return session.close(CloseStatus.REQUIRED_EXTENSION)
//        } else {
//            val isValidate: Boolean = true;
//            if (!isValidate) {
//                return session.close()
//            }
//        }
//        val output = session.receive()
//            .concatMap { mapper ->
//                val msg = mapper.payloadAsText
//                log.info("mapper: $msg")
//                Flux.just(msg)
//            }.map { value ->
//                log.info("value: $value")
//                session.textMessage("Echo $value")
//            }

        return session.send(
            session.receive()
                .map { m -> m.payloadAsText }
                .doOnNext { msg -> log.info("from client: $msg") }
                .map { msg -> session.textMessage("Echo $msg") }
                .doOnSubscribe { e -> log.info("client doOnSubscribe => connect from client[{}] ...", session.id) }
                .doOnTerminate { log.info("client doOnTerminate => close from client[{}] ...", session.id) }
        )
            .doOnError { e -> log.error(e.message) }
            .doOnSubscribe { e -> log.info("server doOnSubscribe => send to client") }
            .doOnTerminate { log.info("server doOnTerminate => close from server") }
           // .doFinally { log.info("server doFinally => send end ...") }
        ;

    }
}