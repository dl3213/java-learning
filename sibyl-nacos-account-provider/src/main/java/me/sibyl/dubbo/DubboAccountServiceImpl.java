package me.sibyl.dubbo;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
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

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    @SentinelResource(
            value = "DubboAccountService.consume",
            blockHandler = "blockHandler",
            fallback = "fallback"
    )
    public Long consume(AccountConsumeRequest orderCreateRequest) {
        log.info("[dubbo]now consume");
        List<UserAccount> userAccountList = userAccountMapper.selectList(
                Wrappers.lambdaQuery(new UserAccount())
                        .eq(UserAccount::getUserId, orderCreateRequest.getUserId())
        );
        UserAccount account = CollectionUtils.isEmpty(userAccountList) ? null : userAccountList.get(0);
        if (Objects.isNull(account)) {
            account = UserAccount.builder()
                    .userId(Long.valueOf(orderCreateRequest.getUserId()))
                    .balance(String.valueOf(100))
                    .state("01")
                    .version(1)
                    .createTime(LocalDateTime.now())
                    .build();
            userAccountMapper.insert(account);
        }

        userAccountMapper.update(
                null,
                Wrappers.lambdaUpdate(new UserAccount())
                        .eq(UserAccount::getId, account.getId())
                        .eq(UserAccount::getVersion, account.getVersion())
                        .set(UserAccount::getBalance, new BigDecimal(account.getBalance()).subtract(orderCreateRequest.getAmount()))
                        .set(UserAccount::getUpdateTime, LocalDateTime.now())
                        .set(UserAccount::getVersion, account.getVersion() + 1)
        );

//        int i = 1 / 0;

        return account.getUserId();
    }

    /**
     * 降级触发
     */
    public Long blockHandler(AccountConsumeRequest orderCreateRequest, BlockException e) {
        return -1L;
    }
    /**
     *  熔断触发
     */
    public Long fallback(AccountConsumeRequest orderCreateRequest) {
        return 0L;
    }
}
