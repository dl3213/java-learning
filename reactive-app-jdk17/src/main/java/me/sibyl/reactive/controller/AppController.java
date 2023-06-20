package me.sibyl.reactive.controller;

import me.sibyl.reactive.domain.QueryRequest;
import me.sibyl.reactive.domain.User;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

/**
 * @author dyingleaf3213
 * @Classname AppController
 * @Description TODO
 * @Create 2023/04/01 22:18
 */
@RestController
@RequestMapping("/api/v1/annotation")
public class AppController {

    @GetMapping("/get")
    public Mono<User> get() {
        return Mono.just(new User("dl3213", "dl3213"));
    }

    @PostMapping("/post")
    public Mono<ResponseEntity<?>> post(@Validated QueryRequest queryRequest,
                                        BindingResult bindingResult,
                                        UriComponentsBuilder builder) throws MethodArgumentNotValidException {
        System.err.println(new Object() {
        }.getClass().getEnclosingMethod());

        if (System.currentTimeMillis() % 2 == 0) {
            throw new MethodArgumentNotValidException(new MethodParameter(new Object() {
            }.getClass().getEnclosingMethod(), 0), bindingResult);
        }

        return Mono.just(
                ResponseEntity
                        .created(builder.path("/api/v1/annotation/get")
                                .path(queryRequest.getId()).build().toUri()).build()
        );
    }
}
