package me.sibyl.controller;

import me.sibyl.util.thread.ThreadUtil;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

/**
 * @author dyingleaf3213
 * @Classname WebFluxController
 * @Description TODO
 * @Create 2023/03/26 20:21
 */
@RestController
public class WebFluxController {

    public static void main(String[] args) {
        Flux.just(1, 2, 3, "test")
                .publishOn(Schedulers.single())
                .subscribe(ThreadUtil::out);
        ThreadUtil.join();
    }
}
