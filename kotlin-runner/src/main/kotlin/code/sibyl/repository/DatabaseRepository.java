package code.sibyl.repository;

import code.sibyl.aop.DS;
import code.sibyl.domain.database.Database;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;

@Repository
//@DS("default")
//@DS("bi-1")
public interface DatabaseRepository extends R2dbcRepository<Database, Long> {
    @Query("SELECT * from T_BASE_DATABASE ")
    Flux<Database> list();

    Flux<Database> findAllByTypeIn(Collection<String> type);
}
