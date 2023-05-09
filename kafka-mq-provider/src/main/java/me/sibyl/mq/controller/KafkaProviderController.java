package me.sibyl.mq.controller;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @Classname KafkaProviderController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/09 21:51
 */
@RestController
@RequestMapping("/mq/kafka/provider")
@Slf4j
public class KafkaProviderController {

    @Resource
    KafkaTemplate<String,String> kafkaTemplate;

    @RequestMapping("/send")
    public Response send(String msg){
        kafkaTemplate.send("sibyl", msg);
        return Response.success();
    }
}
