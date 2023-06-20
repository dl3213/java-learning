package me.sibyl.reactive.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * @author dyingleaf3213
 * @Classname TestController
 * @Description TODO
 * @Create 2023/04/02 01:48
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello() {
        var start = System.currentTimeMillis();
        var helloStr = getHelloStr();
        System.out.println("普通接口耗时：" + (System.currentTimeMillis() - start));
        System.err.println(Thread.currentThread().getName());
        return helloStr;
    }

    @GetMapping("/helloWebFlux")
    public Mono<String> hello0() {
        var start = System.currentTimeMillis();
        var hello0 = Mono.fromSupplier(this::getHelloStr);
        System.out.println("WebFlux 接口耗时：" + (System.currentTimeMillis() - start));
        System.err.println(Thread.currentThread().getName());
        return hello0;
    }

    private String getHelloStr() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello";
    }

}
