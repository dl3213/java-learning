package me.sibyl.cas;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
/**
 * @Classname CertificateAuthorityApplication
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 09:52
 */
@SpringBootApplication
@MapperScan("me.sibyl.cas.mapper")

public class CasApplication {
    public static void main(String[] args) {
        SpringApplication.run(CasApplication.class,args);
    }
}
