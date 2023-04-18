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
        intFlowRule();
        SpringApplication.run(NacosAccountProvider.class, args);
    }

    private static void intFlowRule() {
        List<FlowRule> ruleList = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("DubboAccountService.consume");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(2);
        ruleList.add(rule);
        FlowRuleManager.loadRules(ruleList);
    }

}
