package code.sibyl.service;

import code.sibyl.common.r;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Service
@Slf4j
public class Eos2Service {

//    @Autowired
//    @Qualifier("eos-test")
    private R2dbcEntityTemplate eosTemplate;

    public static Eos2Service getBean() {
        return r.getBean(Eos2Service.class);
    }

    public Mono<Long> test() {
        final long start = System.currentTimeMillis();
        System.err.println(eosTemplate);
        DatabaseClient client = eosTemplate.getDatabaseClient();
        Flux<Map<String, Object>> 发货 = get发货(client);
        Flux<Map<String, Object>> 退货 = get退货(client);
        return 发货.concatWith(退货)
//                .delayElements(Duration.ofMillis(1L))
//                .delayElements(Duration.ofNanos())
//                .map(item -> {
//                    System.err.println(item);
//                    return item;
//                })

                .reduce(new HashMap<String, List>(), (map, e) -> {
                    String contractCode = String.valueOf(e.get("contract_code"));
                    if (!map.containsKey(contractCode)) {
                        map.put(contractCode, new ArrayList<Map>());
                    }
                    ((List<Map>) map.get(contractCode)).add(e);
                    return map;
                })
                .flatMapMany(map -> Flux.fromIterable(map.entrySet()))


//                .groupBy(item -> String.valueOf(item.get("contract_code"))) // 不适用于大型数据
//                .flatMap(group -> Mono.just(group.key()).zipWith(group.count())) // 执行很久

                .map(tuple -> {
                    System.err.println(tuple.getKey() + " -> " + tuple.getValue().size());
                    return tuple.getValue().size();
                })
                .reduce((a, b) -> a + b)
//                .then()
//                .thenReturn(1L)
//                .count()
                .map(count -> {
                    log.info("[物资发退货报表] count = {}", count);
                    return count;
                })
                .then()
                .thenReturn(1L)
                .doFinally(signalType -> log.info("[物资发退货报表] cost = {}", (System.currentTimeMillis() - start)));
    }

    @NotNull
    private static Flux<Map<String, Object>> get发货(DatabaseClient client) {
        Flux<Map<String, Object>> 发货 = client.sql("""
                        select store.region_company_code,
                               store.region_company_name,
                               store.sub_region_code,
                               store.sub_region_name,
                               contract.delivery_warehouse                         store_code,
                               store.store_name,
                               contract_link.dept_code,
                               contract_link.dept_name,
                               main.contract_code,
                               contract.project_name,
                               mat.material_code,
                               m.material_name,
                               m.material_model,
                               m.theorentical_weight,
                               date_format(main.document_date, '%Y-%m-%d')      as date,
                               sum(mat.demand_quantity)                         as primary_quantity,
                               sum(mat.demand_quantity * m.theorentical_weight) as ton
                        from th_crm_plan_deliver_mat mat
                                 left join th_material_info m on m.material_code = mat.material_code
                                 left join th_crm_plan_deliver main on mat.deliver_plan_id = main.id
                                 left join th_crm_contract contract on contract.contract_code = main.contract_code
                                 left join th_crm_contract_business_model contract_link on contract_link.contract_code = contract.contract_code
                                 left join th_mater_store_info store on store.store_code = contract.delivery_warehouse
                        where main.is_del = '0'
                          and date_format(main.document_date, '%Y-%m') = '2024-12'
                        group by store.region_company_code,
                                 store.region_company_name,
                                 store.sub_region_code,
                                 store.sub_region_name,
                                 contract.delivery_warehouse,
                                 store.store_name,
                                 contract_link.dept_code,
                                 contract_link.dept_name,
                                 main.contract_code,
                                 contract.project_name,
                                 mat.material_code,
                                 m.material_name,
                                 m.material_model,
                                 m.theorentical_weight,
                                 date_format(main.document_date, '%Y-%m-%d')
                        """)
                .fetch()
                .all()
//                .takeLast(1000)
                ;
        return 发货;
    }

    @NotNull
    private static Flux<Map<String, Object>> get退货(DatabaseClient client) {
        Flux<Map<String, Object>> 退货 = client.sql("""       
                        select store.region_company_code,
                               store.region_company_name,
                               store.sub_region_code,
                               store.sub_region_name,
                               contract.delivery_warehouse                          store_code,
                               store.store_name,
                               contract_link.dept_code,
                               contract_link.dept_name,
                               main.sales_contract                                  contract_code,
                               contract.project_name,
                               mat.material_code,
                               m.material_name,
                               m.material_model,
                               m.theorentical_weight,
                               date_format(main.document_date, '%Y-%m-%d')       as date,
                               sum(mat.primary_quantity)                         as primary_quantity,
                               sum(mat.primary_quantity * m.theorentical_weight) as ton
                        from th_war_rent_check_mat mat
                                 left join th_material_info m on m.material_code = mat.material_code
                                 left join th_war_rent_check main on mat.p_id = main.id
                                 left join th_crm_contract contract on contract.contract_code = main.sales_contract
                                 left join th_crm_contract_business_model contract_link on contract_link.contract_code = contract.contract_code
                                 left join th_mater_store_info store on store.store_code = contract.delivery_warehouse
                        where main.is_del = '0'
                          and date_format(main.document_date, '%Y-%m') = '2024-12'
                        group by store.region_company_code,
                                 store.region_company_name,
                                 store.sub_region_code,
                                 store.sub_region_name,
                                 contract.delivery_warehouse,
                                 store.store_name,
                                 contract_link.dept_code,
                                 contract_link.dept_name,
                                 main.sales_contract,
                                 contract.project_name,
                                 mat.material_code,
                                 m.material_name,
                                 m.material_model,
                                 m.theorentical_weight,
                                 date_format(main.document_date, '%Y-%m-%d')
                        """)
                .fetch()
                .all()
//                .takeLast(1000)
                ;
        return 退货;
    }
}
