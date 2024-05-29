package code.sibyl

import code.sibyl.common.Response
import code.sibyl.database.Repository
import io.vertx.core.*
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.templates.SqlTemplate
import java.util.*


class Server : AbstractVerticle() {

    override fun start() {
//        var vertxOptions = VertxOptions()
//            .setWorkerPoolSize(32)
//            .setUseDaemonThread(false)
//            .setMaxWorkerExecuteTime(3 * 60L * 1000 * 1000000)//默认1分钟
//        vertx = Vertx.vertx(vertxOptions)
//        println(vertx.toString() + " in start")

        Repository.getInstance().builder(vertx)
        Repository.getInstance().test()
        println(Repository.getInstance())
        val router: Router = Router.router(vertx)
        //router.route().handler(StaticHandler.create());
        router.route().handler(BodyHandler.create());// body必须
        //router.route().handler(HSTSHandler.create());
//        // 添加跨域支持的中间件
//        val allowedHeaders: MutableSet<String> = HashSet()
//        allowedHeaders.add("x-requested-with")
//        allowedHeaders.add("Access-Control-Allow-Origin")
//        allowedHeaders.add("origin")
//        allowedHeaders.add("Content-Type")
//        allowedHeaders.add("accept")
//        allowedHeaders.add("X-PINGARUNER")
//        allowedHeaders.add("X-PINGARUNER")
//        allowedHeaders.add("Content-Security-Policy")
//        val allowedMethods: MutableSet<HttpMethod> = HashSet<HttpMethod>()
//        allowedMethods.add(HttpMethod.GET)
//        allowedMethods.add(HttpMethod.POST)
//        allowedMethods.add(HttpMethod.OPTIONS)
//        router.route().handler(CorsHandler.create("*").allowedMethods(allowedMethods).allowCredentials(true).addOrigin("*"))

//        router.route("/*")
//            .handler { context -> println("request => /*"); context.next() };
        router.get("/")
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };
        router.get("/index")
            .handler { context -> context.response().putHeader("content-type", "text/html").end("Hello World!") };

        //在租物资汇总表测试
        router
            .route("/default/finance/threport/com.primeton.finance.report.report_mat_cz.biz.ext")
            .consumes("application/json")
            .produces("application/json")
            .handler { context ->
                println(Thread.currentThread().name)
                println(Thread.currentThread().threadGroup.name)
                var requestJson = context.body().asJsonObject()
                println(requestJson)
                var sql = """
                    select contract.contract_code             as sales_contract,
                       contract.project_name              as project_name,
                       contract.org_code                  as org_code,
                       contract.org_name                  as org_name,
                       contract.customer_code             as cust_code,
                       contract.customer_name             as cust_name,
                       contract.region                    as region,
                       region.DICTNAME                    as region_name,
                       contract.delivery_warehouse        as delivery_warehouse,
                       store.store_name                   as store_name,
                       store.region_company               as region_company,
                       region_company.DICTNAME            as region_company_name,
                       rent_out_mat.material_code         as material_code,
                       mat.material_name                  as material_name,
                       mat.material_model                 as specification_and_model,
                       rent_out_mat.unit_ton_weight       as unit_ton_weight,
                       rent_out_mat.settlement_ton_weight as settlement_ton_weight,
                       sum(rent_out_mat.primary_quantity) as primary_quantity,
                       1                                  as ret
                from th_crm_contract contract
                         left join th_mater_store_info store on store.store_code = contract.delivery_warehouse
                         left join thlease_eos.eos_dict_entry region_company
                                   on region_company.DICTTYPEID = 'region_company' and region_company.dictid = store.region_company
                         left join thlease_eos.eos_dict_entry region
                                   on region.DICTTYPEID = 'crm_region' and region.dictid = contract.region
                         left join th_crm_rent_out rent_out
                                   on rent_out.sales_contract = contract.contract_code and rent_out.is_del = '0'
                         left join th_crm_rent_out_mat rent_out_mat on rent_out_mat.p_id = rent_out.id
                         left join th_material_info mat on mat.material_code = rent_out_mat.material_code
                where contract.is_del = '0'
                group by contract.contract_code,
                         contract.project_name,
                         contract.org_code,
                         contract.org_name,
                         contract.customer_code,
                         contract.customer_name,
                         contract.region,
                         contract.delivery_warehouse,
                         store.store_name,
                         store.region_company,
                         rent_out_mat.material_code,
                         mat.material_name,
                         mat.material_model,
                         rent_out_mat.unit_ton_weight,
                         rent_out_mat.settlement_ton_weight
                """.trimIndent();
                sql = "select * from th_crm_contract where is_del = '0'";
                println("sql => $sql")
                val parameters = Collections.singletonMap<String, Any>("contractCode", "EAC20230005")
                SqlTemplate
                    .forQuery(Repository.getInstance().jdbcPool(), sql)
                    .mapTo(Row::toJson)
                    .execute(parameters)
                    .onFailure { error -> error.printStackTrace() }
                    .onSuccess { rows -> context.json(Response.success(rows)) }
            }
        vertx
            .createHttpServer()
            .requestHandler(router)
            .listen(33060);

        //先后组合
        var start = System.currentTimeMillis()
        val future = anAsyncAction()
        future.compose(this::anotherAsyncAction)
            .onComplete { ar ->
                if (ar.failed()) {
                    println("Something bad happened")
                    ar.cause().printStackTrace()
                } else {
                    System.out.println("Result: " + ar.result())
                }
                println("compose cost => " + (System.currentTimeMillis() - start))
            }

        //同时
        var start2 = System.currentTimeMillis()
        Future.all(anAsyncAction(), anotherAsyncAction("test")).onComplete { ar ->
            if (ar.failed()) {
                println("Something bad happened")
                ar.cause().printStackTrace()
            } else {
                System.out.println("Result: " + ar.result().size())
            }
            println("all cost => " + (System.currentTimeMillis() - start2))
        }
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
    }
}
