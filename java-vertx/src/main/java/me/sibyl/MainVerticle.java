package me.sibyl;

import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.Log4JLoggerFactory;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import me.sibyl.router.AppRouterBuilder;

/**
 * @author dyingleaf3213
 * @Classname VertxApp
 * @Description TODO
 * @Create 2023/03/27 21:24
 */

@Slf4j
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
//        System.err.println(log);

//        System.setProperty("vertx.logger-delegate-factory-class-name","io.vertx.core.logging.Log4j2LogDelegateFactory");

        // Create a Router
        Router router = Router.router(vertx);

        //
        router = AppRouterBuilder.build(router);

        // Create the HTTP server
        vertx.createHttpServer()
                // Handle every request using the router
                .requestHandler(router)
                // Start listening
                .listen(8888)
                // Print the port
                .onSuccess(server -> {
                    log.info("HTTP server started on port " + server.actualPort());

                    log.trace("i am trace.{}");
                    log.debug("i am debug.");
                    log.info("i am info.");
                    log.warn("i am warn.");
                    log.error("i am error.");
                });
    }
}
