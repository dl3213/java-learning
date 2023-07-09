package me.sibyl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.entity.UserAccount;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author dyingleaf3213
 * @Classname AppTest
 * @Description TODO
 * @Create 2023/04/05 20:24
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = NacosAccountProvider.class)
@Slf4j
public class accountProviderTest {

    @Resource
    private UserAccountMapper userAccountMapper;
    @Resource
    private Redisson redisson;

    @Test
    public void testLock() {

        Stream.iterate(1, i -> i + 1).limit(10).parallel().forEach(i -> {
            RLock redissonLock = redisson.getLock("sibyl");
            synchronized (this) {
                UserAccount account = userAccountMapper.selectOne(
                        Wrappers.lambdaQuery(new UserAccount())
                                .eq(UserAccount::getUserId, "dl3213")
                                .eq(UserAccount::getState, "1")
                );
                System.err.println(i + " b4 =>" + account.getBalance());

                redissonLock.lock();
//
                BigDecimal balance = new BigDecimal(account.getBalance());
                if (balance.compareTo(BigDecimal.ZERO) <= 0) return;
                account.setBalance(balance.subtract(BigDecimal.ONE).toString());
                int update = userAccountMapper.updateById(account);
                System.err.println(i + " update => " + update);

                System.err.println(i + " after => " + account.getBalance());
//
                redissonLock.unlock();
            }
        });

        System.err.println("end");

//        try {
//            Thread.currentThread().join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }
}
