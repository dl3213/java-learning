package me.sibyl;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dyingleaf3213
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("me.sibyl")
public class NacosAccountProvider {

    public static void main(String[] args) {
        SpringApplication.run(NacosAccountProvider.class, args);
    }

}
