package code.sibyl

import code.sibyl.common.Response
import code.sibyl.database.Repository
import io.vertx.core.AbstractVerticle
import io.vertx.core.Launcher
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.templates.SqlTemplate
import java.util.*


class Main : AbstractVerticle() {

    override fun start() {
        Repository.getInstance().builder(vertx)
        println(Repository.getInstance())
        val router: Router = Router.router(vertx)
        //router.route().handler(StaticHandler.create());
        router.route().handler(BodyHandler.create());// body必须
//        router.route("/*")
//            .handler { context -> println("request => /*"); context.next() };
        router.get("/")
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };
        router.get("/index")
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };

        //在租物资汇总表测试
        router.route("/default/finance/threport/com.primeton.finance.report.report_mat_cz.biz.ext")
            .consumes("application/json")
            .produces("application/json")
            .handler { context ->
                var requestJson = context.body().asJsonObject()
                println(requestJson)
                var sql = "SELECT * from th_crm_rent_out where is_del = '0' and sales_contract = #{contractCode}";
//                var sql = "SELECT now()";
                val parameters = Collections.singletonMap<String, Any>("contractCode", "EAC20230005")
//                Repository.getInstance().jdbcPool().query(sql).execute()
//                    .onFailure { error -> error.printStackTrace() }
//                    .onSuccess { rows ->  context.json(Response.success(rows.map { it -> it.toJson() }.toList())) }
                SqlTemplate
                    .forQuery(Repository.getInstance().jdbcPool(), sql)
                    .mapTo(Row::toJson)
                    .execute(parameters)
                    .onFailure { error -> error.printStackTrace() }
                    .onSuccess { rows -> context.json(Response.success(rows)) }
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