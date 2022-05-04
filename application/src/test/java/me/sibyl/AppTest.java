package me.sibyl;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname AppTest
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/04/01 21:15
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,classes = Application.class)
@Slf4j
public class AppTest {

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
