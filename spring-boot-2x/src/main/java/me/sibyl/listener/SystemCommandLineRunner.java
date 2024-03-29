package me.sibyl.listener;

import me.sibyl.service.AppService;
import me.sibyl.util.SpringUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

//@Component
@Order(9)
@ConditionalOnExpression("${runnerEnabled:false}")
public class SystemCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.err.println("CommandLineRunner");
        System.err.println(SpringUtil.getBean(AppService.class));
    }
}
