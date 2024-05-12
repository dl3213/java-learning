package code.sibyl.repository.eos;

import code.sibyl.aop.DS;
import code.sibyl.domain.database.Database;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Repository
@DS("thlease_db")
public interface EosRepository extends R2dbcRepository<Database, Long> {


    @Query("SELECT sum(a.weighing_weight) as rentAogWeight " +
            "FROM th_war_rent_aog a " +
            "where is_del = '0' " +
            "  and TO_DAYS(document_date) = TO_DAYS(now())")
    Mono<BigDecimal> 今日退货吨重();

    @Query("SELECT sum(a.weighing_weight) as rentAogWeight " +
            "FROM th_war_rent_aog a " +
            "where is_del = '0' " +
            "  and TO_DAYS(document_date) = TO_DAYS(now())")
    Mono<BigDecimal> 今日发货吨重();

    @Query("SELECT sum(a.weighing_weight) as rentAogWeight " +
            "FROM th_war_rent_aog a " +
            "where is_del = '0' " +
            "  and TO_DAYS(document_date) = TO_DAYS(now())")
    Mono<BigDecimal> 自有资产();

    @Query("SELECT sum(a.weighing_weight) as rentAogWeight " +
            "FROM th_war_rent_aog a " +
            "where is_del = '0' " +
            "  and TO_DAYS(document_date) = TO_DAYS(now())")
    Mono<BigDecimal> 转租库存();
}
