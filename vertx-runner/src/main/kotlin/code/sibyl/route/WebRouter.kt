package code.sibyl.route

import code.sibyl.common.Response
import code.sibyl.database.Repository
import code.sibyl.dto.ContractDTO
import code.sibyl.service.EosQueryService
import io.vertx.core.Future
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.sqlclient.Row
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.templates.SqlTemplate

class WebRouter private constructor() {

    private lateinit var router: io.vertx.ext.web.Router;

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
                //获取合同数
                EosQueryService.getInstance()
                    .queryContractList(requestJson)
                    .compose { r -> EosQueryService.getInstance().queryRentOutList(r) }
                    .onFailure { err -> err.printStackTrace() }
                    .onSuccess { ret -> context.json(Response.success(ret)) }
                //同时获取 出租单和归还单数据

                //println("body => $requestJson")
//                queryRentOut(requestJson)
//                    //.compose(this::queryRentRecycle)
//                    .onFailure { error -> error.printStackTrace() }
//                    .onSuccess { rows ->
//                        //println("first query end ==> " + rows.size())
//                        this.queryRentRecycle(rows).onSuccess {
//                           // println("return end ==> " + rows.size())
//                            //rows.forEach { println(it) }
//                            //println(rows.result().size())
//                            //var toList = rows.toList().map { item -> queryRentOut(item) }.toList()
//                            //toList.forEach { it -> println(it) }
//                            //queryRentRecycle(null).onSuccess { println(it) }
//
//                            //context.json(Response.success(rows))
//                        }
//                        context.json(Response.success(rows))
//                    }
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
                         rent_out_mat.settlement_ton_weight;
                """.trimIndent();
        return SqlTemplate
            .forQuery(Repository.getInstance().jdbcPool(), sql)
            .mapTo(Row::toJson)
            .execute(requestJson.map);

    }


    private fun queryRentRecycle(rows: RowSet<JsonObject>): Future<RowSet<JsonObject>> {
        var sql = """
                    select main.sales_contract,mat.material_code,sum(ifnull(mat.primary_quantity,0)) as back_num
                    from th_war_rent_recycle main
                    left join th_war_rent_recycle_mat mat on mat.p_id = main.id
                    where main.is_del = '0' and main.sales_contract = #{sales_contract} and mat.material_code = #{material_code}
                    group by main.sales_contract,mat.material_code;
                """.trimIndent();
        sql = """
                    select sum(ifnull(mat.primary_quantity,0)) as back_num
                    from th_war_rent_recycle main
                    left join th_war_rent_recycle_mat mat on mat.p_id = main.id
                    where main.is_del = '0' 
                    and main.sales_contract = #{sales_contract}
                    and mat.material_code = #{material_code}
                """.trimIndent();
        println("back_num ==> ")
        return Future.succeededFuture(rows.onEach { row ->
            //println(it)
            SqlTemplate
                .forQuery(Repository.getInstance().jdbcPool(), sql)
                .mapTo { r -> r.toJson().first() }
                .execute(row.map)
                .compose {
                    //println("get_back -> " + it.first().value)
                    row.put("back_num", it.first().value)
                    return@compose Future.succeededFuture(row);
                }
        });

//        return rows.flatMap { it ->
//            println(it)
//            SqlTemplate
//                .forQuery(Repository.getInstance().jdbcPool(), sql)
//                .mapTo(Row::toJson)
//                .execute(it.map)
//        }

//        rows.toList().stream().peek { it -> SqlTemplate
//            .forQuery(Repository.getInstance().jdbcPool(), sql)
//            .mapTo(Row::toJson)
//            .execute(it.map) }.co
//
//
//        return SqlTemplate
//            .forQuery(Repository.getInstance().jdbcPool(), sql)
//            .mapTo(Row::toJson)
//            .execute(row?.map);

    }

}