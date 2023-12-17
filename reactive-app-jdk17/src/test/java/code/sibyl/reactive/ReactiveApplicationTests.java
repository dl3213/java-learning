package code.sibyl.reactive;

import jakarta.annotation.Resource;
import code.sibyl.reactive.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
class ReactiveApplicationTests {

    @Resource
    private UserDao userDao;

    @Test
    void contextLoads() {

        System.err.println(userDao);

        System.err.println("contextLoads");
    }

    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4, 5).map(e -> "e" + e).doOnNext(e -> {
            System.err.println("doOnNext " + e);
        }).then(Mono.just("test")).subscribe(s -> {
            System.err.println(s);
        });
    }
}
