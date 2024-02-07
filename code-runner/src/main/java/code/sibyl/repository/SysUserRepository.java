package code.sibyl.repository;

import code.sibyl.domain.Database;
import code.sibyl.domain.SysUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface SysUserRepository extends R2dbcRepository<SysUser, Long> {
    @Query("SELECT * from t_sys_user ")
    Flux<SysUser> list();
}
