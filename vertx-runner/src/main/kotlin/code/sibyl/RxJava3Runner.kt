package code.sibyl

import io.vertx.core.Launcher
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.pgclient.PgConnectOptions
import io.vertx.rxjava3.core.AbstractVerticle
import io.vertx.rxjava3.ext.web.Router
import io.vertx.rxjava3.pgclient.PgPool
import io.vertx.rxjava3.sqlclient.templates.RowMapper
import io.vertx.rxjava3.sqlclient.templates.SqlTemplate
import io.vertx.sqlclient.PoolOptions
import org.slf4j.LoggerFactory
import java.util.*


//https://vertx.io/docs/
class RxJava3Runner : AbstractVerticle() {

    private val log = LoggerFactory.getLogger(RxJava3Runner::class.java)
    private var port = 8088;

    override fun start() {
        val router: Router = Router.router(vertx)
        router.route()
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };
        vertx.createHttpServer().requestHandler(router).listen(port);
        var pid = ProcessHandle.current().pid()
        println("java.version = ${System.getProperty("java.version")}")
        println("RxJava3Runner[${pid}] listen in ${port}...")


        val connectOptions = PgConnectOptions()
            .setPort(5432)
            .setHost("127.0.0.1")
            .setDatabase("postgres")
            .setUser("postgres")
            .setPassword("sibyl-postgres-0127")

        // Pool options
        val poolOptions = PoolOptions()
            .setMaxSize(5)

        val pgPool: PgPool = PgPool.pool(vertx, connectOptions, poolOptions)

        SqlTemplate.forQuery(pgPool, "select * from t_sys_user where 1 = 1")
            .mapTo(RowMapper<JsonObject> { row -> row.toJson() })
            .rxExecute(HashMap())
            .doOnSuccess { rowSet ->
                println(rowSet::class.java)
                for (hashMap in rowSet) {
                    println(hashMap)
                }

            }
            .subscribe()

    }

    override fun stop() {
        super.stop()
        println("RxJava3Runner stop...")
    }
}

fun main(args: Array<String>) {
    Launcher.executeCommand("run", RxJava3Runner::class.java.getName())
}