package code.sibyl

import code.sibyl.common.Response
import code.sibyl.database.Repository
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Launcher
import io.vertx.core.buffer.Buffer
import io.vertx.ext.web.Router
import io.vertx.mysqlclient.impl.util.BufferUtils
import java.util.stream.Collectors


class Main : AbstractVerticle() {

    override fun start() {
        var builder = Repository().builder(vertx)
        println(builder)
        val router: Router = Router.router(vertx)
        //router.route().handler(StaticHandler.create());
        router.get("/")
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };
        router.get("/index")
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };

        router.route("/test").respond { context ->
            println(context.request().absoluteURI())
            val start = System.currentTimeMillis()
            var sql = "SELECT * FROM th_crm_rent_out where is_del = '0'";
            println(start)
//            builder.sql(sql) .onComplete { ar ->
//                if (ar.succeeded()) {
//                    //ar.result().forEach { item -> println(item.toJson()) }
//                    println("Done")
//                    System.err.println("cost => " + (System.currentTimeMillis() - start))
//                    var jsonObjectList = ar.result().map { item -> item.toJson() }
//                    //return@onComplete Future.succeededFuture(jsonObjectList);
//                } else {
//                    println("Something went wrong " + ar.cause().message)
//                }
//            }
//            return@respond builder.sql(sql).result().map{item -> item.toJson()}.stream().collect(Collectors.toList());
            builder.sql(sql)
                .onComplete { ar ->
                    var toList = ar.result().map { it.toJson() }.toList()
                    println(toList.size)
                    //Future.succeededFuture(Response.success(toList))
                    Response.success(toList)
                }
            //return@respond Future.succeededFuture(Response.success())
//                .map { item -> item.toJson() }
//                .toList()
//                .let { i -> Future.succeededFuture(i) }

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