package code.sibyl.reactive.router;

import org.springframework.lang.Nullable;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;
import org.springframework.validation.Validator;
import reactor.util.function.Tuple2;

/**
 * @author dyingleaf3213
 * @Classname AppControllerHelper
 * @Description TODO
 * @Create 2023/04/01 23:01
 */

public class AppControllerHelper {

    public static <T> Mono<T> requestBody2Mono(ServerRequest serverRequest, Validator validator, Class<T> clazz) {
        return serverRequest.bodyToMono(clazz);
    }

    private static <T> Mono<T> validate(Validator validator, Mono bodyToMono) {
        return null;
    }


    @FunctionalInterface
    public interface MyValidator<T> {
        Mono<Tuple2<T, Exception>> validate(T t, Exception e);
    }
}
