package me.sibyl.dubbo;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.nacos.common.utils.ThreadUtils;
import com.baomidou.lock.annotation.Lock4j;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.entity.UserAccount;
import me.sibyl.microservice.provider.nacos.DubboAccountService;
import me.sibyl.microservice.request.AccountConsumeRequest;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author dyingleaf3213
 * @Classname BubboAccountServiceImpl
 * @Description TODO
 * @Create 2023/03/19 20:45
 */
@Service
@DubboService
@Slf4j
public class DubboAccountServiceImpl implements DubboAccountService {
    @Resource
    private UserAccountMapper userAccountMapper;

    public final static String resourceName = "DubboAccountService.consume";

//    @PostConstruct
//    public void init(){
//        List<FlowRule> rules = new ArrayList<>();
//        FlowRule rule = new FlowRule();
//        rule.setResource(resourceName);
//        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
//        rule.setCount(1);
//        rules.add(rule);
//        FlowRuleManager.loadRules(rules);
//        log.info("FlowRuleManager.loadRules => done");
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
//    @SentinelResource(
//            value = resourceName
//            ,
//            blockHandler = "blockHandler"
//            ,
//            fallback = "fallback"
//    )
    @Lock4j(keys = "#request.userId", expire = 10000, acquireTimeout = 5000)
    public Long consume(AccountConsumeRequest request) {
        log.info("[dubbo]now consume");
        List<UserAccount> userAccountList = userAccountMapper.selectList(
                Wrappers.lambdaQuery(new UserAccount())
                        .eq(UserAccount::getUserId, request.getUserId())
        );
        UserAccount account = CollectionUtils.isEmpty(userAccountList) ? null : userAccountList.get(0);
        if (Objects.isNull(account)) {
            account = UserAccount.builder()
                    .userId(Long.valueOf(request.getUserId()))
                    .balance(String.valueOf(100))
                    .state("01")
                    .version(1)
                    .createTime(LocalDateTime.now())
                    .build();
            userAccountMapper.insert(account);
        }
        boolean amount_allow_negative = false;
        if (new BigDecimal(account.getBalance()).compareTo(BigDecimal.ZERO) <= 0 && !amount_allow_negative) {
            throw new RuntimeException("余额不足");
        }
        ThreadUtils.sleep(500);

        // 1.
//        account.setBalance(String.valueOf(new BigDecimal(account.getBalance()).subtract(request.getAmount())));
//        userAccountMapper.updateById(account);

        // 2.
        userAccountMapper.update(
                null,
                Wrappers.lambdaUpdate(new UserAccount())
                        .eq(UserAccount::getId, account.getId())
                        .eq(UserAccount::getVersion, account.getVersion())
                        .set(UserAccount::getBalance, new BigDecimal(account.getBalance()).subtract(request.getAmount()))
                        .set(UserAccount::getUpdateTime, LocalDateTime.now())
                        .set(UserAccount::getVersion, account.getVersion() + 1)
        );

//        int i = 1 / 0;

        return account.getUserId();
    }

    /**
     * 降级触发-优先级高，前提上配置了规则，没有规则，则进入熔断触发
     */
    public Long blockHandler(AccountConsumeRequest orderCreateRequest, BlockException e) {
        log.error("[account]消费异常-降级触发");
        log.error(e.getClass().getName());
        e.printStackTrace();
        return 0L;
    }

    /**
     * 熔断触发 - 异常触发
     */
    public Long fallback(AccountConsumeRequest orderCreateRequest, Throwable throwable) {
        log.error("[account]消费异常-熔断触发");
        log.error(throwable.getClass().getName());
        throwable.printStackTrace();
        return -1L;
    }
}
