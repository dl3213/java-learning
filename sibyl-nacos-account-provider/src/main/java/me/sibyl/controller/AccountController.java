package me.sibyl.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.dubbo.DubboAccountServiceImpl;
import me.sibyl.entity.UserAccount;
import me.sibyl.microservice.request.AccountConsumeRequest;
import me.sibyl.service.AccountService;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/19 04:49
 */
@RestController
@RequestMapping("/api/v1/account")
@RefreshScope
public class AccountController {

    @Autowired
    private DubboAccountServiceImpl dubboAccountService;

    @Resource
    private AccountService accountService;

    @PostMapping("/consume")
    public String consume(AccountConsumeRequest request) {
        String consume = dubboAccountService.consume(request);
        System.err.println(consume);
        return String.valueOf(System.currentTimeMillis());
    }

    @GetMapping("/query/{userId}")
    public String query(@PathVariable String userId) {
        System.err.println(accountService.queryByUserId(userId));
        return String.valueOf(System.currentTimeMillis());
    }

    @Resource
    private UserAccountMapper userAccountMapper;
    @Resource
    private Redisson redisson;

    @GetMapping("/test")
    public String test() {
        RLock redissonLock = redisson.getLock("sibyl");
        redissonLock.lock();
        UserAccount account = userAccountMapper.selectOne(
                Wrappers.lambdaQuery(new UserAccount())
                        .eq(UserAccount::getUserId, "dl3213")
                        .eq(UserAccount::getState, "1")
        );
//
        BigDecimal balance = new BigDecimal(account.getBalance());
        System.err.println("balance => " + balance);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        account.setBalance(balance.subtract(BigDecimal.ONE).toString());
        int update = userAccountMapper.updateById(account);
        System.err.println("update => " + account.getBalance());

        redissonLock.unlock();

//        synchronized (this) {
//        }
        return String.valueOf(System.currentTimeMillis());
    }
}
