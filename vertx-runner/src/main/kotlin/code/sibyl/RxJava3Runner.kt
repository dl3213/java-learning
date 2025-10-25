package code.sibyl

import io.vertx.core.Launcher
import io.vertx.rxjava3.core.AbstractVerticle
import io.vertx.rxjava3.ext.web.Router
import org.slf4j.LoggerFactory


class RxJava3Runner : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(RxJava3Runner::class.java)
    var port = 8088;

    override fun start() {
        val router: Router = Router.router(vertx)
        router.route()
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };
        vertx.createHttpServer().requestHandler(router).listen(port);
        var pid = ProcessHandle.current().pid()
        log.info("java.version = {}", System.getProperty("java.version"))
        log.info("RxJava3Runner[${pid}] listen in ${port}...")
    }

    override fun stop() {
        super.stop()
        log.info("RxJava3Runner stop...")
    }
}

fun main(args: Array<String>) {
    Launcher.executeCommand("run", RxJava3Runner::class.java.getName())
}