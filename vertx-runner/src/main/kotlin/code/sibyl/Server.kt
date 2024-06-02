package code.sibyl

import code.sibyl.database.Repository
import code.sibyl.route.WebRouter
import io.vertx.core.AbstractVerticle
import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Promise
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import java.util.*


class Server : AbstractVerticle() {

    override fun start() {
        Repository.getInstance().builder(vertx)
        Repository.getInstance().test()
        println(Repository.getInstance())
        var webRouter = WebRouter.getInstance().build(vertx)
        var port = 33060;
        vertx
            .createHttpServer()
            .requestHandler(webRouter)
            .listen(port).onComplete { handler ->
                println("server start in $port")
            };

//        //先后组合
//        var start = System.currentTimeMillis()
//        val future = anAsyncAction()
//        future.compose(this::anotherAsyncAction)
//            .onComplete { ar ->
//                if (ar.failed()) {
//                    println("Something bad happened")
//                    ar.cause().printStackTrace()
//                } else {
//                    System.out.println("Result: " + ar.result())
//                }
//                println("compose cost => " + (System.currentTimeMillis() - start))
//            }
//
        //同时
//        var start2 = System.currentTimeMillis()
//        Future.all(anAsyncAction(), anotherAsyncAction("test")).onComplete { ar ->
//            if (ar.failed()) {
//                println("Something bad happened")
//                ar.cause().printStackTrace()
//            } else {
//                System.out.println("Result: " + ar.result().size())
//            }
//            println("all cost => " + (System.currentTimeMillis() - start2))
//        }
    }

    private fun anAsyncAction(): Future<String> {
        val promise: Promise<String> = Promise.promise()
        // mimic something that take times
        vertx.setTimer(100) { l: Long? -> promise.complete("world") }
        return promise.future()
    }

    private fun anotherAsyncAction(name: String): Future<String> {
        val promise: Promise<String> = Promise.promise()
        // mimic something that take times
        vertx.setTimer(100) { l: Long? -> promise.complete("hello $name") }
        return promise.future()
    }


    override fun stop() {
        super.stop()
        println("server stopped...")
    }
}