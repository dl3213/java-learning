package me.sibyl.controller;

import me.sibyl.socket.SocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/websocket")
public class WebSocketController {
    @Autowired
    private SocketClient webSocketClient;


    @GetMapping("/sendMessage")
    public String sendMessage(String message) {
        webSocketClient.groupSending(message);
        return message;
    }
}
