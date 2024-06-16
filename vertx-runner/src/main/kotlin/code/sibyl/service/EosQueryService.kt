package code.sibyl.service

import code.sibyl.database.Repository
import code.sibyl.dto.ContractDTO
import io.vertx.codegen.annotations.Nullable
import io.vertx.core.Future
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.sqlclient.RowSet
import io.vertx.sqlclient.Tuple
import java.util.*
import java.util.stream.Collector
import java.util.stream.Collectors


class EosQueryService private constructor() {
    fun test(contractList: RowSet<ContractDTO>): Future<Tuple> {
        println("test")
        return Future.succeededFuture(Tuple.of(contractList));
    }

    fun queryRentOutList(contractList: RowSet<ContractDTO>): Future<RowSet<JsonObject>> {
        println("queryRentOutList")
        var toList = contractList.map{e -> e.contractCode}.toSet();
            //contractList.toList().stream().map { e -> e.contractCode }.collect(Collectors.toList())
        var sql = """
                    select 
                        main.sales_contract,
                        mat.material_code,
                        sum(mat.primary_quantity) as primary_quantity
                from th_crm_rent_out main
                         left join th_crm_rent_out_mat mat on mat.p_id = main.id
                where main.is_del = '0' and main.sales_contract in (#{contractCodeList})
                group by main.sales_contract, mat.material_code
                """.trimIndent();
        println(toList)
        var jsonArray = JsonArray()
        toList.forEach { jsonArray.add(it) }
        println(jsonArray)
        val parameters = Collections.singletonMap<String, Any>("contractCodeList", jsonArray)
        return io.vertx.sqlclient.templates.SqlTemplate
            .forQuery(Repository.getInstance().jdbcPool(), sql)
            .mapTo { r -> r.toJson() }
            .execute(parameters).onFailure { err -> err.printStackTrace() };
    }

    fun queryRentRecycleList(contractList: RowSet<ContractDTO>): Future<RowSet<JsonObject>> {
        println("queryRentRecycleList")
        var toList = contractList.map { ContractDTO::getContractCode }.toList()
        var sql = """
                    select main.sales_contract,mat.material_code,sum(ifnull(mat.primary_quantity,0)) as back_num
                    from th_war_rent_recycle main
                    left join th_war_rent_recycle_mat mat on mat.p_id = main.id
                    where main.is_del = '0' 
                    and main.sales_contract in (#{contractCodeList})
                    group by main.sales_contract,mat.material_code
                """.trimIndent();
        val parameters = Collections.singletonMap<String, Any>("contractCodeList", toList)
        return io.vertx.sqlclient.templates.SqlTemplate
            .forQuery(Repository.getInstance().jdbcPool(), sql)
            .mapTo { r -> r.toJson() }
            .execute(parameters);
    }

    fun queryContractList(requestJson: @Nullable JsonObject?): Future<RowSet<ContractDTO>> {
        println("queryContractList")
        var sql = """
                    select contract.contract_code         as sales_contract,
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
                       region_company.DICTNAME            as region_company_name
                from th_crm_contract contract
                         left join th_mater_store_info store on store.store_code = contract.delivery_warehouse
                         left join thlease_eos.eos_dict_entry region_company on region_company.DICTTYPEID = 'region_company' and region_company.dictid = store.region_company
                         left join thlease_eos.eos_dict_entry region on region.DICTTYPEID = 'crm_region' and region.dictid = contract.region
                where contract.is_del = '0' 
                """.trimIndent();

        return io.vertx.sqlclient.templates.SqlTemplate
            .forQuery(Repository.getInstance().jdbcPool(), sql)
            .mapTo { r ->
                var dto = ContractDTO()
                dto.contractCode = r.getString("sales_contract")
                dto.projectName = r.getString("project_name")
                dto.orgCode = r.getString("org_code")
                dto.orgName = r.getString("org_name")
                dto.customerCode = r.getString("cust_code")
                dto.customerName = r.getString("cust_name")
                dto.region = r.getString("region")
                dto.regionName = r.getString("region_name")
                dto.deliveryWarehouse = r.getString("delivery_warehouse")
                dto.storeName = r.getString("store_name")
                dto.regionCompany = r.getString("region_company")
                dto.regionCompanyName = r.getString("region_company_name")
                return@mapTo dto
            }
            .execute(requestJson?.map);
    }

    companion object {
        private var instance: EosQueryService? = null

        fun getInstance(): EosQueryService {
            if (instance == null) {
                instance = EosQueryService()
            }
            return instance!!
        }
    }
}