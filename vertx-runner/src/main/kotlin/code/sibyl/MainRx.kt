package code.sibyl

import code.sibyl.database.Repository
import io.vertx.core.Launcher
import io.vertx.rxjava3.core.AbstractVerticle
import io.vertx.rxjava3.ext.web.Router


class MainRx : AbstractVerticle() {

    override fun start() {
        val router: Router = Router.router(vertx)
        //router.route().handler(StaticHandler.create());
        router.route()
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };
        Repository.getInstance().builder(vertx)
        vertx.createHttpServer().requestHandler(router).listen(8889);
    }

    override fun stop() {
        super.stop()
    }
}

fun main() {
    Launcher.executeCommand("run", MainRx::class.java.getName())
}