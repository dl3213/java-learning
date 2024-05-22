package code.sibyl

import code.sibyl.common.Response
import code.sibyl.database.Repository
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Launcher
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Router


class Main : AbstractVerticle() {

    override fun start() {
        var builder = Repository().builder(vertx)
        println(builder)
        val router: Router = Router.router(vertx)
        //router.route().handler(StaticHandler.create());
//        router.get("/")
//            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };
        router.get("/index")
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };

        router.route("/test")
            .failureHandler { error ->
                println("failureHandler")
                println(error)
                error.failure().printStackTrace()
            }
            .handler { context ->
                println(context.request().absoluteURI())
                val start = System.currentTimeMillis()
                var sql = "SELECT * from th_crm_rent_out where is_del = '0'";
                println(start)
                builder.sql(sql)
                    .onComplete { ar ->
                        var toList = ar.result().map { it.toJson() }.toList()
                        //toList.forEach { item -> println(item) }
                        println(toList.size)
                        context.response().putHeader("Content-Type", "application/json")
                            .end(Response.success(toList).encode())
                    }

            }
        vertx.createHttpServer().requestHandler(router).listen(8888);
    }

    override fun stop() {
        super.stop()
    }
}

fun main() {
    Launcher.executeCommand("run", Main::class.java.getName())
}