package code.sibyl.repository;

import code.sibyl.domain.database.Database;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

//@Repository
public interface CommonRepository {

    @Query("SELECT * from T_BASE_DATABASE ")
    Flux<Database> list();

}
