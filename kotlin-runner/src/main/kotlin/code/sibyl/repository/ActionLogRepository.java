package code.sibyl.repository;

import code.sibyl.domain.ActionLog;
import code.sibyl.domain.database.Database;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.List;

@Repository
public interface ActionLogRepository extends R2dbcRepository<ActionLog, Long> {

}
