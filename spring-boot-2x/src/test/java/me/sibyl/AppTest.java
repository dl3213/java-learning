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
import java.util.stream.Collectors;

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

        for (int i = 0; i < 1000000; i++) {
            User user = userList.get(RandomUtils.nextInt(0, userList.size() - 1));
            PsychoPassRecord record = new PsychoPassRecord();
            record.setUid(user.getId());
            record.setPsychoPass(String.valueOf(RandomUtils.nextDouble(0.01d, 500.00d)));
            record.setType("0");
            record.setCreateId(user.getId());
            record.setCreateTime(LocalDateTime.now());
            psychoPassRecordMapper.insert(record);
        }
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
