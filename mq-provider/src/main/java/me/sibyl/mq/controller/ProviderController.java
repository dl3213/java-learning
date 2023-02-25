package me.sibyl.mq.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.mq.rabbit.provider.ConfirmMqProvider;
import me.sibyl.mq.rabbit.provider.DelayedMqProvider;
import me.sibyl.mq.rabbit.provider.TopicMqProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @Classname ProviderController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/22 21:06
 */

@RestController
@RequestMapping("/mq/provider")
@Slf4j
public class ProviderController {

    @Resource
    private ConfirmMqProvider confirmMqProvider;
    @Resource
    private DelayedMqProvider delayedMqProvider;
    @Resource
    private TopicMqProvider topicMqProvider;

    @GetMapping("/topic/send")
    public void topicSend(String msg) {
        topicMqProvider.send(msg);
    }

    @GetMapping("/delayed/send")
    public void send(String msg, Integer ttl) {
        log.info("provider --------- time = {}, send = {}, ttl = {} ", LocalDateTime.now(), msg, ttl);
        delayedMqProvider.send(msg, ttl);
    }

    @GetMapping("/confirm/send")
    public void confirmSend(String msg) {
        confirmMqProvider.send(msg);
    }
}
