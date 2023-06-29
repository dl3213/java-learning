package me.sibyl.config;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.net.URI;

@Configuration
@Slf4j
public class WebSocketConfig {

    /**
     * 这个bean的注册,用于扫描带有@ServerEndpoint的注解成为websocket,如果你使用外置的tomcat就不需要该配置文件
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        log.info("[ServerEndpointExporter]init");
        return new ServerEndpointExporter();
    }

//    @Bean
//    @DependsOn(value = {"webSocket", "serverEndpointExporter"})
    public WebSocketClient webSocketClient() {
        try {
            WebSocketClient webSocketClient = new WebSocketClient(new URI("ws://127.0.0.1:80/web/socket/test"), new Draft_6455()) {

                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    log.info("[WebSocketClient] 连接成功");
                }

                @Override
                public void onMessage(String message) {
                    log.info("[WebSocketClient] 收到消息={}", message);

                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    log.info("[WebSocketClient] 退出连接");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                    log.info("[WebSocketClient] 连接错误={}", ex.getMessage());
                }
            };
            webSocketClient.connectBlocking();
            while (!webSocketClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
                log.info("[WebSocketClient]正在连接...");
            }
            log.info("[WebSocketClient]连接成功...");
            return webSocketClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
