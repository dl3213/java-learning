package me.sibyl.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ConfigurableBootstrapContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.time.Duration;

//@Component
//@ConditionalOnExpression("${runnerEnabled:false}")
@Slf4j
public class AppRunner implements SpringApplicationRunListener {

    private final SpringApplication application;
    private final String[] args;

    public AppRunner(SpringApplication application, String[] args) {
        System.err.println("AppRunner");
        this.application = application;
        this.args = args;
    }

    @Override
    public void starting(ConfigurableBootstrapContext bootstrapContext) {
        System.err.println("starting");
        SpringApplicationRunListener.super.starting(bootstrapContext);
    }

    @Override
    public void started(ConfigurableApplicationContext context, Duration timeTaken) {
        System.err.println("started");
        SpringApplicationRunListener.super.started(context, timeTaken);
    }

    @Override
    public void ready(ConfigurableApplicationContext context, Duration timeTaken) {
        System.err.println("ready");
        SpringApplicationRunListener.super.ready(context, timeTaken);
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        System.err.println("failed");
        SpringApplicationRunListener.super.failed(context, exception);
    }
}
