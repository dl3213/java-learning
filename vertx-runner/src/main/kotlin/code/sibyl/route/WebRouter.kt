package code.sibyl.route

import code.sibyl.common.Response
import code.sibyl.database.Repository
import io.vertx.core.Future
import io.vertx.core.Future.future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.coAwait
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.kotlin.sqlclient.templates.executeAwait
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.templates.SqlTemplate
import kotlinx.coroutines.GlobalScope
import java.math.BigDecimal
import java.util.*
import java.util.stream.Collectors
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class WebRouter private constructor() {

    lateinit var router: io.vertx.ext.web.Router;

    companion object {
        private var instance: WebRouter? = null

        fun getInstance(): WebRouter {
            if (instance == null) {
                instance = WebRouter()
            }
            return instance!!
        }
    }

    fun router(): io.vertx.ext.web.Router {
        return this.router;
    }

    fun build(vertx: Vertx): io.vertx.ext.web.Router {
        val router: io.vertx.ext.web.Router = io.vertx.ext.web.Router.router(vertx)
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
                var requestJson = context.body().asJsonObject()
                println("body => $requestJson")
                queryRentOut(requestJson)
                    .onFailure { error -> error.printStackTrace() }
                    .onSuccess { rows ->
                        println("first query end ==> ")
                        //println(rows.result().size())
                        var toList = rows.toList().map { item -> queryRentOut(item) }.toList()
                        //toList.forEach { it -> println(it) }
                        queryRentRecycle(null).onSuccess { println(it) }
                        context.json(Response.success())
                    }
            }
        this.router = router;
        return router;
    }

    private fun queryRentOut(requestJson: JsonObject): Future<RowSet<JsonObject>> {
        //println(Thread.currentThread().name)
        //println(Thread.currentThread().threadGroup.name)
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
                where contract.is_del = '0' and contract.contract_code = 'SZ20240291'
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
                         rent_out_mat.settlement_ton_weight;
                """.trimIndent();
        return SqlTemplate
            .forQuery(Repository.getInstance().jdbcPool(), sql)
            .mapTo(Row::toJson)
            .execute(requestJson.map);

    }

    private fun queryRentRecycle(row: JsonObject?): Future<RowSet<JsonObject>> {
        //println(Thread.currentThread().name)
        //println(Thread.currentThread().threadGroup.name)
        var sql = """
                    select main.sales_contract,mat.material_code,sum(ifnull(mat.primary_quantity,0)) as back_num
                    from th_war_rent_recycle main
                    left join th_war_rent_recycle_mat mat on mat.p_id = main.id
                    where main.is_del = '0' and main.sales_contract = #{sales_contract} and mat.material_code = #{material_code}
                    group by main.sales_contract,mat.material_code;
                """.trimIndent();
        sql = """
                    select sum(ifnull(mat.primary_quantity,0))
                    from th_war_rent_recycle main
                    left join th_war_rent_recycle_mat mat on mat.p_id = main.id
                    where main.is_del = '0' 
                    and main.sales_contract = 'SZ20240291'
                    and mat.material_code = '3302010014'
                """.trimIndent();
        return SqlTemplate
            .forQuery(Repository.getInstance().jdbcPool(), sql)
            .mapTo(Row::toJson)
            .execute(row?.map);

    }

}