package me.sibyl.cas;

import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Classname CasApplication
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 09:52
 */
@SpringBootApplication
@MapperScan("me.sibyl.cas.mapper")
@ComponentScan("me.sibyl")
public class CasApplication {
    public static void main(String[] args) {
        SpringApplication.run(CasApplication.class,args);
    }
}
