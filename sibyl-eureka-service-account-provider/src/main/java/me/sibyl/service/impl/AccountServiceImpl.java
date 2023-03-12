package me.sibyl.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.entity.UserAccount;
import me.sibyl.microservice.request.AccountConsumeRequest;
import me.sibyl.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Classname ServiceProvider2Impl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 06:25
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Override
    @Transactional
    public String consume(AccountConsumeRequest orderCreateRequest) {
        List<UserAccount> userAccountList = userAccountMapper.selectList(
                Wrappers.lambdaQuery(new UserAccount())
                        .eq(UserAccount::getUserId, orderCreateRequest.getUserId())
        );
        UserAccount account = CollectionUtils.isEmpty(userAccountList) ? null : userAccountList.get(0);
        if (Objects.isNull(account)) {
            account = UserAccount.builder()
                    .userId(orderCreateRequest.getUserId())
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

        //int i = 1 / 0;

        return account.getUserId();
    }
}
