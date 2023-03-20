package me.sibyl;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.PsychoPassRecordMapper;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.PsychoPassRecord;
import me.sibyl.entity.User;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = SpringBoot2xApplication.class)
@Slf4j
public class AppTest {

    @Resource
    private PsychoPassRecordMapper psychoPassRecordMapper;
    @Resource
    private UserMapper userMapper;

    public static void main(String[] args) {
        System.err.println("test");
        System.out.println("test");
    }

    @Test
    public void dbTest(){

        List<User> userList = userMapper.selectList(null);

//        List<PsychoPassRecord> psychoPassRecords = psychoPassRecordMapper.selectList(null);
//        for (PsychoPassRecord psychoPassRecord : psychoPassRecords) {
//            System.err.println(psychoPassRecord);
//        }

        List<String> strings = Arrays.asList("sibyl", "test", "dev", "prod","dl3213","steam");

        Stream.iterate(1, a -> a + 1).limit(1000000).parallel().forEach(i ->{
            User user = userList.get(RandomUtils.nextInt(0, userList.size() - 1));
            PsychoPassRecord record = new PsychoPassRecord();
            record.setUid(user.getId());
            record.setPsychoPass(String.valueOf(RandomUtils.nextDouble(0.01d, 500.00d)));
            record.setType("0");
            record.setCreateId(user.getId());
            record.setFlag(String.valueOf(System.currentTimeMillis()%5));
            record.setState(String.valueOf(System.currentTimeMillis()%5));
            record.setCode(strings.get(RandomUtils.nextInt(0, strings.size() - 1)) + (UUID.randomUUID().toString().substring(0, 10)));
            record.setCreateTime(LocalDateTime.now());
            psychoPassRecordMapper.insert(record);
        });

    }

    @Test
    public void test01(){
        //flatMap(对流扁平化处理)
        String[] words = new String[]{"Hello","World"};

        List a = Arrays.stream(words)
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());
        a.forEach(System.out::print);
    }
}
