package code.sibyl.service

import code.sibyl.common.r
import com.alibaba.fastjson2.JSONObject
import org.apache.http.client.utils.URLEncodedUtils
import org.slf4j.LoggerFactory
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.stereotype.Component
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.ConcurrentHashMap

// @MessageMapping和@SendTo
@Component
class DataBaseSocket : WebSocketHandler {

    private val log = LoggerFactory.getLogger(DataBaseSocket::class.java)
    private var clientMap: Map<WebSocketSession, DatabaseClient> = ConcurrentHashMap();

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


        var id : Long =  handshakeInfo.uri.path.replace("/database/socket/", "").toLong()
        log.info("socket get => id = {}", id)
        var queryList = URLEncodedUtils.parse(handshakeInfo.uri.query, StandardCharsets.UTF_8)
        var queryMap = queryList.associateBy { it.name to it.value }
        log.error("socket get => map = {}", queryMap)
        return session.send(
            session.receive()
                .map { m -> m.payloadAsText }
                .flatMap { text -> Mono.zip( Mono.just(text), DataBaseService.getBean().findById(id)) } // todo
                .doOnNext { tuple ->
                    log.info("from client: ${tuple.t1}")
                    clientMap += session to DatabaseClient.create(DataBaseService.getBean().getConnectionFactoryByDatabaseEntity(tuple.t2))
                }
                .flatMap { tuple ->

                    if(!r.isSql(tuple.t1)){
                        return@flatMap Mono.just(tuple.t1);
                    }else{
                        return@flatMap clientMap[session]?.sql(tuple.t1)
                            ?.fetch()?.all()?.collectList()
                            ?.onErrorResume { throwable ->
                                throwable.printStackTrace()
                                Mono.just(Arrays.asList(hashMapOf("throwable" to throwable.message)))
                            }
                    }

                }
                .map { tuple ->
                    println(clientMap)
                    session.textMessage(JSONObject.toJSONString(tuple))
                }
                .doOnError { e -> e.printStackTrace() }
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