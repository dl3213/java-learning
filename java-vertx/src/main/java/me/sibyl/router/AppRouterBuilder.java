package me.sibyl.router;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import me.sibyl.common.response.Response;

public class AppRouterBuilder {
    public static Router build(Router router) {
        // The response is returned in JSON
        router.get("/api/v1/touter/get").respond(context -> Future.succeededFuture(Response.success("get")));
        router.post("/api/v1/touter/post").respond(context -> Future.succeededFuture(Response.success("post")));
        // for non JSON responses
        router.get("/some/path").respond(ctx -> ctx.response().putHeader("Content-Type", "text/plain").end("hello world!"));

        router
                .route(HttpMethod.POST, "/post/test/:id/:name/")// 严格/结尾
                .handler(ctx -> {
                    String id = ctx.pathParam("id");
                    System.err.println(id);
                    String name = ctx.pathParam("name");
                    System.err.println(name);

                    // Do something with them...
                    ctx.json(Response.success());
                });

        // Mount the handler for all incoming requests at every path and HTTP method
        router.route().handler(context -> {
            // Get the address of the request
            String address = context.request().connection().remoteAddress().toString();
            // Get the query parameter "name"
            MultiMap queryParams = context.queryParams();
            String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";
            // Write a json response
            context.json(
                    new JsonObject()
                            .put("name", name)
                            .put("address", address)
                            .put("message", "Hello " + name + " connected from " + address)
            );
        });
        return router;
    }
}
