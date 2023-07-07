package me.sibyl.listener;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.service.AppService;
import me.sibyl.util.SpringUtil;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.net.URI;

/**
 * @author dyingleaf3213
 * @Classname SystemApplicationRunner
 * @Description TODO
 * @Create 2023/06/05 20:36
 */
@Component
@Order(8)
@ConditionalOnExpression("${runnerEnabled:false}")
@Slf4j
public class SystemApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.err.println("ApplicationRunner");
        System.err.println(SpringUtil.getBean(AppService.class));
//        System.err.println(SpringUtil.getBean(SecurityConfig.class));

        WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:8080/web/socket/test"), new Draft_6455()) {
            //连接服务端时触发
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                log.info("websocket客户端和服务器连接成功");
            }
            //收到服务端消息时触发
            @Override
            public void onMessage(String message) {
                log.info("websocket客户端收到消息={}", message);
            }
            //和服务端断开连接时触发
            @Override
            public void onClose(int code, String reason, boolean remote) {
                log.info("websocket客户端退出连接");
            }
            //连接异常时触发
            @Override
            public void onError(Exception ex) {
                log.info("websocket客户端和服务器连接发生错误={}", ex.getMessage());
            }
        };
        webSocketClient.connectBlocking();
        webSocketClient.send("hello");
    }
}
