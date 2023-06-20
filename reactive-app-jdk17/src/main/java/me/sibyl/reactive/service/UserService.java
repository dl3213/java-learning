package me.sibyl.reactive.service;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.reactive.dao.UserDao;
import me.sibyl.reactive.domain.User;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author dyingleaf3213
 * @Classname UserService
 * @Description TODO
 * @Create 2023/04/02 01:35
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;

    private final DatabaseClient databaseClient;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public Mono<User> queryById(String id) {
        return userDao.findById(id);
    }

    public Flux<User> list() {
        return r2dbcEntityTemplate.select(User.class).from("sys_user").all();
    }

    public void add(User user) {
//        databaseClient.in
//        userDao.save(user).log().as(StepVerifier::create).expectNextCount(1).verifyComplete();
        r2dbcEntityTemplate.insert(user).subscribe();
    }

    public void page(){

    }
}
