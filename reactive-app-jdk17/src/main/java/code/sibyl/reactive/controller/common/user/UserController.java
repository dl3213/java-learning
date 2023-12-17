package code.sibyl.reactive.controller.common.user;

import code.sibyl.reactive.domain.User;
import code.sibyl.reactive.service.UserService;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryMetadata;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @Classname UserController
 * @Description TODO
 * @Create 2023/04/02 01:36
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/query/{id}")
    public Mono<User> queryById(@PathVariable String id) {
        return userService.queryById(id);
    }

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public Flux<User> all() {
        return userService.list();
    }

    @PostMapping("/add")
    public void add(User user) {
        userService.add(user);
    }


    /**
     * r2dbc - connectionFactory
     * jdbc - datasource
     */
    @Resource
    private ConnectionFactory connectionFactory;

    @GetMapping("/test")
    public void test() {
        System.err.println(connectionFactory);
        System.err.println(connectionFactory.getClass());
        ConnectionFactoryMetadata metadata = connectionFactory.getMetadata();
        System.err.println(metadata.getName());
        System.err.println(metadata.getClass());
    }

    @PostMapping("/post/test")
    public void testPost() {
        System.err.println(connectionFactory);
        System.err.println(connectionFactory.getClass());
        ConnectionFactoryMetadata metadata = connectionFactory.getMetadata();
        System.err.println(metadata.getName());
        System.err.println(metadata.getClass());
    }
}
