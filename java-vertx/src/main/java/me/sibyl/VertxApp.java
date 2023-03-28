package me.sibyl;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

/**
 * @author dyingleaf3213
 * @Classname VertxApp
 * @Description TODO
 * @Create 2023/03/27 21:24
 */

public class VertxApp extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {

        Router router = Router.router(vertx);

        router.get("/test").handler(context -> {
            HttpServerRequest request = context.request();
            String id = request.getParam("id");
            System.err.println("test get => id =" + id);
            context.response().end("test end");
        });

        vertx.createHttpServer().requestHandler(router).listen(80,
                http -> {
                    if (http.succeeded()) {
                        startPromise.complete();
                        System.out.println("HTTP server started on port 8888");
                    } else {
                        startPromise.fail(http.cause());
                    }
                }
        );
    }

    public static void main(String[] args) throws Exception {
        new VertxApp().start(null);
    }
}
