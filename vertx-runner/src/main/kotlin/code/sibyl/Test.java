package code.sibyl;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class Test {
    public static void main(String[] args) {
        // 假设我们有一个JsonArray列表
        JsonArray list = new JsonArray();
        list.add(new JsonObject().put("item", "item1"));
        list.add(new JsonObject().put("item", "item2"));
        list.add(new JsonObject().put("item", "item3"));

        // 使用reduce进行遍历处理
        Future<Void> result = list.stream()
                .map(JsonObject.class::cast)
                .reduce(Future.succeededFuture(), (future, json) -> future.compose(v -> {
                    // 处理每个item的逻辑
                    System.out.println("Processing item: " + json.getString("item"));
                    // 假设这里是异步操作，我们返回一个Future
                    return Future.succeededFuture();
                }), (f1, f2) -> f2); // 这里使用f2的结果

        // 设置结果并结束
        result.onComplete(ar -> {
            if (ar.succeeded()) {
                System.err.println("succeeded");
            } else {
                ar.cause().printStackTrace();
            }
        });
    }
}
