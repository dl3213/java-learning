package code.sibyl

import code.sibyl.common.r
import code.sibyl.database.Repository
import code.sibyl.service.BeanService
import code.sibyl.service.SystemService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.vertx.core.Launcher
import io.vertx.rxjava3.core.AbstractVerticle
import io.vertx.rxjava3.ext.web.Router


class RxJava3Runner : AbstractVerticle() {

    override fun start() {
        val router: Router = Router.router(vertx)
        //router.route().handler(StaticHandler.create());
        router.route()
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };
        Repository.getInstance().builder(vertx).subscribe()
//        SystemService.getInstance().pixiv_init(vertx)
//        Repository.getInstance().test()
        vertx.createHttpServer().requestHandler(router).listen(9000);
        println("RxJava3Runner listen in 9000...")
        //println(BeanService.getMap())
        r.sleep(5000)
        SystemService.getInstance().pixiv_init_parallel(vertx)
    }

    override fun stop() {
        super.stop()
        println("RxJava3Runner stop...")
    }
}

fun main() {
    println("java.version -> " + System.getProperty("java.version"))
    Launcher.executeCommand("run", RxJava3Runner::class.java.getName())
}