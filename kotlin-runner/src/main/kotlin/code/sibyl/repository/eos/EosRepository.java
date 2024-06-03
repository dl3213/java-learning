package code.sibyl.repository.eos;

import code.sibyl.aop.DS;
import code.sibyl.domain.user.SysUser;
import code.sibyl.dto.QueryDTO;
import code.sibyl.dto.QueryMap;
import code.sibyl.dto.RentRecycleDTO;
import code.sibyl.dto.TestDTO;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
@DS("thlease_db" )
public interface EosRepository extends R2dbcRepository<SysUser, Long> {


    @Query("SELECT sum(a.weighing_weight) as rentAogWeight " +
            "FROM th_war_rent_aog a " +
            "where is_del = '0' " +
            "  and TO_DAYS(document_date) = TO_DAYS(now())" )
    Mono<BigDecimal> 今日退货吨重();

    @Query("SELECT sum(a.weighing_weight) as rentAogWeight " +
            "FROM th_war_rent_aog a " +
            "where is_del = '0' " +
            "  and TO_DAYS(document_date) = TO_DAYS(now())" )
    Mono<BigDecimal> 今日发货吨重();

    @Query("SELECT sum(a.weighing_weight) as rentAogWeight " +
            "FROM th_war_rent_aog a " +
            "where is_del = '0' " +
            "  and TO_DAYS(document_date) = TO_DAYS(now())" )
    Mono<BigDecimal> 自有资产();

    @Query("SELECT sum(a.weighing_weight) as rentAogWeight " +
            "FROM th_war_rent_aog a " +
            "where is_del = '0' " +
            "  and TO_DAYS(document_date) = TO_DAYS(now())" )
    Mono<BigDecimal> 转租库存();

    @Query("""
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
                     rent_out_mat.settlement_ton_weight
            """)
    Flux<TestDTO> test(); // SZ20240291 + 3302010014

    @Query("""
            select main.sales_contract,mat.material_code, sum(ifnull(mat.primary_quantity,0)) as back_num
            from th_war_rent_recycle main
            left join th_war_rent_recycle_mat mat on mat.p_id = main.id
            where main.is_del = '0'
            and main.sales_contract = ?sales_contract
            and mat.material_code = ?material_code
            group by main.sales_contract,mat.material_code
            """)
    Flux<RentRecycleDTO> backNum(String sales_contract, String material_code);

    @Query("""
            select sum(ifnull(mat.primary_quantity,0)) as back_num
            from th_war_rent_recycle main
            left join th_war_rent_recycle_mat mat on mat.p_id = main.id
            where main.is_del = '0'
            and main.sales_contract = ?sales_contract
            and mat.material_code = ?material_code
            """)
    Mono<BigDecimal> backNum_from(String sales_contract, String material_code);

    @Query("""
            select main.sales_contract, mat.material_code, sum(ifnull(mat.primary_quantity,0)) as back_num
            from th_war_rent_recycle main
            left join th_war_rent_recycle_mat mat on mat.p_id = main.id
            where main.is_del = '0'
            and main.sales_contract in ('SZ20240291')
            group by main.sales_contract, mat.material_code
            """)
//    Flux<RentRecycleDTO> backNum_from_list();
    Flux<RentRecycleDTO> findByCustomParams(@Param("map" ) Map<String, Object> map);

    @Query("""
            select sum(ifnull(mat.primary_quantity,0))
            from th_war_rent_recycle main
            left join th_war_rent_recycle_mat mat on mat.p_id = main.id
            where main.is_del = '0'
            and main.sales_contract = 'SZ20240291'
            and mat.material_code = '3302010014'
            group by main.sales_contract,mat.material_code
            """)
    Mono<BigDecimal> sumTest();
}
