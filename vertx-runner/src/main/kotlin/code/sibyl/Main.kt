package code.sibyl

import code.sibyl.database.EosRepository
import io.vertx.core.AbstractVerticle
import io.vertx.core.Launcher
import io.vertx.ext.web.Router


class Main : AbstractVerticle() {

    override fun start() {
        val router: Router = Router.router(vertx)
        //router.route().handler(StaticHandler.create());
        router.route()
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };
        EosRepository().builder(vertx)
        vertx.createHttpServer().requestHandler(router).listen(8888);
    }

    override fun stop() {
        super.stop()
    }
}

fun main() {
    Launcher.executeCommand("run", Main::class.java.getName())
}