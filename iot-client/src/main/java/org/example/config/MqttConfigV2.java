package org.example.config;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.example.config.MqttProperties;
import org.example.mqtt.MqttMessageHandle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.ExecutorChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

@Configuration
public class MqttConfigV2 {

    @Autowired
    private MqttProperties mqttProperties;

    @Autowired
    private MqttMessageHandle mqttMessageHandle;


    //Mqtt 客户端工厂 所有客户端从这里产生
    @Bean
    public MqttPahoClientFactory mqttPahoClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setServerURIs(mqttProperties.getHostUrl().split(","));
        options.setUserName(mqttProperties.getUsername());
        options.setPassword(mqttProperties.getPassword().toCharArray());
        factory.setConnectionOptions(options);
        return factory;
    }

    // Mqtt 管道适配器
    @Bean
    public MqttPahoMessageDrivenChannelAdapter adapter(MqttPahoClientFactory factory) {
        return new MqttPahoMessageDrivenChannelAdapter(mqttProperties.getInClientId(), factory, mqttProperties.getDefaultTopic().split(","));
    }

    // 消息消费者 (接收,处理来自mqtt的消息)
    @Bean
    public IntegrationFlow mqttInbound(MqttPahoMessageDrivenChannelAdapter adapter) {
        adapter.setCompletionTimeout(5000);
        adapter.setQos(1);
        return IntegrationFlows.from(adapter)
                .channel(new ExecutorChannel(mqttThreadPoolTaskExecutor()))
                .handle(mqttMessageHandle)
                .get();
    }

    @Bean
    public ThreadPoolTaskExecutor mqttThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 最大可创建的线程数
        int maxPoolSize = 200;
        executor.setMaxPoolSize(maxPoolSize);
        // 核心线程池大小
        int corePoolSize = 50;
        executor.setCorePoolSize(corePoolSize);
        // 队列最大长度
        int queueCapacity = 1000;
        executor.setQueueCapacity(queueCapacity);
        // 线程池维护线程所允许的空闲时间
        int keepAliveSeconds = 300;
        executor.setKeepAliveSeconds(keepAliveSeconds);
        // 线程池对拒绝任务(无线程可用)的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    // 出站处理器 (向 mqtt 发送消息 生产者)
    @Bean
    public IntegrationFlow mqttOutboundFlow(MqttPahoClientFactory factory) {

        MqttPahoMessageHandler handler = new MqttPahoMessageHandler(mqttProperties.getOutClientId(), factory);
        handler.setAsync(true);
        handler.setConverter(new DefaultPahoMessageConverter());
        handler.setDefaultTopic(mqttProperties.getDefaultTopic().split(",")[0]);
        return IntegrationFlows.from("mqttOutboundChannel").handle(handler).get();
    }

}

