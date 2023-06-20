package me.sibyl.listener;

import me.sibyl.service.AppService;
import me.sibyl.util.SpringUtil;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author dyingleaf3213
 * @Classname SystemApplicationRunner
 * @Description TODO
 * @Create 2023/06/05 20:36
 */
@Component
@Order(8)
@ConditionalOnExpression("${runnerEnabled:false}")
public class SystemApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.err.println("ApplicationRunner");
        System.err.println(SpringUtil.getBean(AppService.class));
    }
}
