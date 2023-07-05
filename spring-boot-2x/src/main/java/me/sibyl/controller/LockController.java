package me.sibyl.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import jodd.util.ThreadUtil;
import me.sibyl.common.response.Response;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.entity.UserAccount;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.transaction.annotation.Transactional;
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
@RequestMapping("/api/v1/lock")
public class LockController {

    @Resource
    private UserAccountMapper userAccountMapper;
    @Resource
    private Redisson redisson;

    @GetMapping("/test/consume/one")
    @Transactional
    public Response consumeOne() {
        System.err.println(new Object() {
        }.getClass().getEnclosingMethod());
//        int one = userAccountMapper.consume("3213", BigDecimal.ONE);
        UserAccount account = userAccountMapper.queryByIdWithForUpdate("3213");
        System.err.println("locking");
        System.err.println(account);
        cn.hutool.core.thread.ThreadUtil.sleep(10000);
        account.setBalance(account.getBalance().subtract(BigDecimal.ONE));
        userAccountMapper.updateById(account);
        System.err.println(account);
        System.err.println("end");
        return Response.success();
    }

    @GetMapping("/test/query/one")
    public Response query() {
        System.err.println(new Object() {
        }.getClass().getEnclosingMethod());
        UserAccount account = userAccountMapper.queryByIdWithForUpdate("3213");
        System.err.println(account);
        return Response.success();
    }

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

            BigDecimal balance = (account.getBalance());
            ThreadUtil.sleep(3000);

            account.setBalance(balance.subtract(BigDecimal.ONE));
            int update = userAccountMapper.updateById(account);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            redissonLock.unlock();
        }

        return String.valueOf(System.currentTimeMillis());
    }

    @GetMapping("/synchronized/consume")
    public Response synchronizedConsume(String userId) {
        synchronized (userId.intern()) {
            UserAccount account = userAccountMapper.selectOne(
                    Wrappers.lambdaQuery(new UserAccount())
                            .eq(UserAccount::getUserId, userId)
                            .eq(UserAccount::getState, "01")
            );
            if (Objects.isNull(account)) {
                account = new UserAccount()
                        .setBalance((BigDecimal.TEN))
                        .setUserId(userId)
                        .setState(String.valueOf("01"))
                        .setVersion(0);
                userAccountMapper.insert(account);
            }
            BigDecimal balance = (account.getBalance());
            ThreadUtil.sleep(3000);
            account.setBalance(balance.subtract(BigDecimal.ONE));
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
            BigDecimal balance = (account.getBalance());
            ThreadUtil.sleep(3000);
            account.setBalance(balance.subtract(BigDecimal.ONE));
            int update = userAccountMapper.updateById(account);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            reentrantLock.unlock();
        }
        return Response.success(System.currentTimeMillis());
    }
}
