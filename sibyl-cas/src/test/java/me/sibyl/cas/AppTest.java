package me.sibyl.cas;

import me.sibyl.cache.service.RedisService;
import me.sibyl.cas.mapper.MenuMapper;
import me.sibyl.cas.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Classname AppTest
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 10:16
 */

@SpringBootTest
public class AppTest {

    @Resource
    private UserMapper userMapper;
    @Resource
    private MenuMapper menuMapper;
    @Resource
    private RedisService redisService;

    @Test
    public void test(){
        System.err.println(userMapper.selectList(null));

        //userMapper.selectList(null).forEach(System.err::println);
//        System.err.println(redisUtil);
//        redisUtil.set("test", LocalDateTime.now());
    }
}
