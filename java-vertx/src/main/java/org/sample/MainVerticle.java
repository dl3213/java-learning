package org.sample;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

/**
 * @author dyingleaf3213
 * @Classname VertxApp
 * @Description TODO
 * @Create 2023/03/27 21:24
 */

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        // Create a Router
        Router router = Router.router(vertx);

        //
        //router = AppRouterBuilder.build(router);

        // Create the HTTP server
        vertx.createHttpServer()
                // Handle every request using the router
                .requestHandler(router)
                // Start listening
                .listen(8888)
                // Print the port
                .onSuccess(server -> {
                    System.out.println("HTTP server started on port " + server.actualPort());
                });
    }
}
