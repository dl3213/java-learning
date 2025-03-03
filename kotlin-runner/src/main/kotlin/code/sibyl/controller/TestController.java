package code.sibyl.controller;

import code.sibyl.common.Response;
import code.sibyl.common.r;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/external")
@Slf4j
@RequiredArgsConstructor
public class TestController {

    @Qualifier("virtualExecutor")
    @Autowired
    private AsyncTaskExecutor asyncTaskExecutor;

    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    public Response test() {
        System.err.println(Thread.currentThread());
        System.err.println(Thread.currentThread().getName());
        System.err.println(Thread.currentThread().isVirtual());
        asyncTaskExecutor.submitCompletable(()->{
            System.err.println("asyncTaskExecutor test");
            System.err.println(Thread.currentThread());
            System.err.println(Thread.currentThread().getName());
            System.err.println(Thread.currentThread().isVirtual());
        }).join();

        return Response.success("1");
    }

    @RequestMapping(value = "/test/mono", method = {RequestMethod.GET, RequestMethod.POST})
    public Mono<Response> test_mono() {


        return Mono.just("1")
                .map(e -> {
                    System.err.println(Thread.currentThread());
                    System.err.println(Thread.currentThread().getName());
                    System.err.println(Thread.currentThread().isVirtual());
                    asyncTaskExecutor.submitCompletable(()->{
                        System.err.println("asyncTaskExecutor test");
                        System.err.println(Thread.currentThread());
                        System.err.println(Thread.currentThread().getName());
                        System.err.println(Thread.currentThread().isVirtual());
                    }).join();
                    return Response.success(e);
                });
    }
}
