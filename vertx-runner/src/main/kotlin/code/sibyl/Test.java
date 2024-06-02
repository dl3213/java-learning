package code.sibyl;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Test {
    public static void main(String[] args) {

        Future<String> f = Future.succeededFuture("a");

        Future<String> composeResult = f.compose(a -> Future.succeededFuture(a));

        Future<String> mapResult = f.map(a -> a);

    }
}
