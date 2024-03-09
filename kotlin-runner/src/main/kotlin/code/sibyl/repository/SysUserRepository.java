package code.sibyl.repository;

import code.sibyl.domain.user.SysUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SysUserRepository extends R2dbcRepository<SysUser, Long> {
    @Query("SELECT * from t_sys_user where IS_DELETED = false ")
    Flux<SysUser> list();

    @Query("SELECT * from t_sys_user where IS_DELETED = false and USERNAME = :username limit 1  ")
    Mono<SysUser> selectByUsername(String username);
}
