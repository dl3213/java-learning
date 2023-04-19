package me.sibyl;

import cn.hutool.core.util.ServiceLoaderUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.PsychoPassRecordMapper;
import me.sibyl.dao.UserAccountMapper;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.PsychoPassRecord;
import me.sibyl.entity.User;
import me.sibyl.entity.UserAccount;
import me.sibyl.service.UserService;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.*;
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
    private RestTemplate restTemplate;

    @Test
    public void rest() {
        //ServiceLoader<UserService> loader = ServiceLoaderUtil.getServiceLoader(UserService.class);

        System.err.println(restTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.15)");
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.ALL.getType());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity obj = restTemplate.exchange(
                "https://api.bilibili.com/x/web-interface/view?bvid=BV1eX4y1f7Aa",
                HttpMethod.GET,
                entity,
                JSONObject.class
        );

        System.err.println(obj);
    }

    @Resource
    private PsychoPassRecordMapper psychoPassRecordMapper;
    @Resource
    private UserMapper userMapper;

    public static void main2(String[] args) {
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

        List<String> strings = Arrays.asList("sibyl", "test", "dev", "prod",
                "dl3213", "steam", "reactive", "python", "java", "cpp", "bolshevik");
        List<String> types = Arrays.asList("00", "01", "02");

        Stream.iterate(1, a -> a + 1).limit(300000).parallel().forEach(i -> {
            User user = userList.get(RandomUtils.nextInt(0, userList.size() - 1));
            PsychoPassRecord record = new PsychoPassRecord();
            //String uid = "dl3213";
            record.setUid(String.valueOf(user.getId()));
            record.setPsychoPass(String.valueOf(RandomUtils.nextDouble(0.01d, 500.00d)));
            record.setType(types.get(RandomUtils.nextInt(0, types.size() - 1)));
            record.setCreateId(String.valueOf(user.getId()));
            record.setFlag(String.valueOf(System.currentTimeMillis() % 5));
            record.setState(String.valueOf(System.currentTimeMillis() % 5));
            record.setCode(strings.get(RandomUtils.nextInt(0, strings.size() - 1)) + (UUID.randomUUID().toString().substring(0, 10)));
            //LocalDateTime createTime = randomTime("2023-03");
            record.setCreateTime(LocalDateTime.now());
            psychoPassRecordMapper.insert(record);
        });

    }

    public static void main(String[] args) {
        System.err.println(randomTime("2023-03"));
        System.err.println(randomTime("2023-03"));
        System.err.println(randomTime("2023-03"));
        System.err.println(randomTime("2023-03"));
    }

    private static LocalDateTime randomTime(String str) {
        DateTimeFormatter DATEFORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM");
        DateTimeFormatter DATEFORMATTER = new DateTimeFormatterBuilder().append(DATEFORMATTER1)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, RandomUtils.nextInt(1, 31))
                .parseDefaulting(ChronoField.HOUR_OF_DAY, RandomUtils.nextInt(0, 23))
                .parseDefaulting(ChronoField.MINUTE_OF_HOUR, RandomUtils.nextInt(0, 59))
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, RandomUtils.nextInt(0, 59))
                .parseDefaulting(ChronoField.NANO_OF_SECOND, RandomUtils.nextInt(0, 99))
                .toFormatter();
        LocalDateTime time = LocalDateTime.parse(str, DATEFORMATTER);
        return time;
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
