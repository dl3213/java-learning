package me.sibyl.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jodd.util.ThreadUtil;
import me.sibyl.common.response.Response;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.entity.UserAccount;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Classname AppController
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/19 04:49
 */
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {

    @Resource
    private UserAccountMapper userAccountMapper;
    @Resource
    private Redisson redisson;

    @GetMapping("/redisson/consume")
    public String redissonLock() {
        System.err.println(Thread.currentThread().getName() + " => " + LocalDateTime.now());
        RLock redissonLock = redisson.getLock("3213");
        redissonLock.lock();
        try {
            UserAccount account = userAccountMapper.selectOne(
                    Wrappers.lambdaQuery(new UserAccount())
                            .eq(UserAccount::getUserId, "3213")
                            .eq(UserAccount::getState, "01")
            );

            BigDecimal balance = new BigDecimal(account.getBalance());
            ThreadUtil.sleep(3000);

            account.setBalance(balance.subtract(BigDecimal.ONE).toString());
            int update = userAccountMapper.updateById(account);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            redissonLock.unlock();
        }

        return String.valueOf(System.currentTimeMillis());
    }

    @GetMapping("/synchronized/consume")
    public Response synchronizedConsume(String userId) {
        synchronized (userId.intern()){
            UserAccount account = userAccountMapper.selectOne(
                    Wrappers.lambdaQuery(new UserAccount())
                            .eq(UserAccount::getUserId, userId)
                            .eq(UserAccount::getState, "01")
            );
            if(Objects.isNull(account)){
                account = new UserAccount()
                        .setBalance(String.valueOf(BigDecimal.TEN))
                        .setUserId(userId)
                        .setState(String.valueOf("01"))
                        .setVersion(0);
                userAccountMapper.insert(account);
            }
            BigDecimal balance = new BigDecimal(account.getBalance());
            ThreadUtil.sleep(3000);
            account.setBalance(balance.subtract(BigDecimal.ONE).toString());
            int update = userAccountMapper.updateById(account);
        }
        System.err.println(LocalDateTime.now());
        return Response.success(System.currentTimeMillis());
    }

    public static ReentrantLock reentrantLock = new ReentrantLock();

    @GetMapping("/lock/consume")
    public Response lockConsume(String userId) {
        System.err.println(Thread.currentThread().getName() + " => " + LocalDateTime.now());
        reentrantLock.lock();
        try {
            UserAccount account = userAccountMapper.selectOne(
                    Wrappers.lambdaQuery(new UserAccount())
                            .eq(UserAccount::getUserId, "3213")
                            .eq(UserAccount::getState, "01")
            );
            BigDecimal balance = new BigDecimal(account.getBalance());
            ThreadUtil.sleep(3000);
            account.setBalance(balance.subtract(BigDecimal.ONE).toString());
            int update = userAccountMapper.updateById(account);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }finally {
            reentrantLock.unlock();
        }
        return Response.success(System.currentTimeMillis());
    }
}
