package me.sibyl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.PsychoPassRecordMapper;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.PsychoPassRecord;
import me.sibyl.entity.User;
import me.sibyl.entity.UserAccount;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Classname AppTest
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/04/01 21:15
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = SpringBoot2xApplication.class)
@Slf4j
public class springboot2test {

    @Resource
    private PsychoPassRecordMapper psychoPassRecordMapper;
    @Resource
    private UserMapper userMapper;

    public static void main(String[] args) {
        System.err.println("test");
        System.out.println("test");
    }

    @Test
    public void dbTest() {

        List<User> userList = userMapper.selectList(null);

//        List<PsychoPassRecord> psychoPassRecords = psychoPassRecordMapper.selectList(null);
//        for (PsychoPassRecord psychoPassRecord : psychoPassRecords) {
//            System.err.println(psychoPassRecord);
//        }

        List<String> strings = Arrays.asList("sibyl", "test", "dev", "prod", "dl3213", "steam");

        Stream.iterate(1, a -> a + 1).limit(1000000).parallel().forEach(i -> {
            User user = userList.get(RandomUtils.nextInt(0, userList.size() - 1));
            PsychoPassRecord record = new PsychoPassRecord();
            record.setUid(user.getId());
            record.setPsychoPass(String.valueOf(RandomUtils.nextDouble(0.01d, 500.00d)));
            record.setType("0");
            record.setCreateId(user.getId());
            record.setFlag(String.valueOf(System.currentTimeMillis() % 5));
            record.setState(String.valueOf(System.currentTimeMillis() % 5));
            record.setCode(strings.get(RandomUtils.nextInt(0, strings.size() - 1)) + (UUID.randomUUID().toString().substring(0, 10)));
            record.setCreateTime(LocalDateTime.now());
            psychoPassRecordMapper.insert(record);
        });

    }

    @Test
    public void test01() {
        //flatMap(对流扁平化处理)
        String[] words = new String[]{"Hello", "World"};

        List a = Arrays.stream(words)
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        a.forEach(System.out::print);
    }

//    @Resource
//    private UserAccountMapper userAccountMapper;
//    @Resource
//    private Redisson redisson;
//
//    @Test
//    public void testLock() {
//
//        Stream.iterate(1, i -> i + 1).limit(10).parallel().forEach(i -> {
//            RLock redissonLock = redisson.getLock("sibyl");
//            synchronized (this) {
//
//                UserAccount account = userAccountMapper.selectOne(
//                        Wrappers.lambdaQuery(new UserAccount())
//                                .eq(UserAccount::getUserId, "dl3213")
//                                .eq(UserAccount::getState, "1")
//                );
//                System.err.println(i + " b4 =>" + account.getBalance());
//
//                redissonLock.lock();
////
//                BigDecimal balance = new BigDecimal(account.getBalance());
//                if (balance.compareTo(BigDecimal.ZERO) <= 0) return;
//                account.setBalance(balance.subtract(BigDecimal.ONE).toString());
//                int update = userAccountMapper.updateById(account);
//                System.err.println(i + " update => " + update);
//
//                System.err.println(i + " after => " + account.getBalance());
////
//                redissonLock.unlock();
//            }
//        });
//
//        System.err.println("end");
//
////        try {
////            Thread.currentThread().join();
////        } catch (InterruptedException e) {
////            e.printStackTrace();
////        }
//    }
}
