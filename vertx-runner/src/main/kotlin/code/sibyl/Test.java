package code.sibyl;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Test {
    public static void main(String[] args) {
        System.out.println(System.getProperty("java.version"));
//        Future<String> f = Future.succeededFuture("a");
//
//        Future<String> composeResult = f.compose(a -> Future.succeededFuture(a));
//
//        Future<String> mapResult = f.map(a -> a);
//
//        String collect = Arrays.asList("1", "2", 3).stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}
