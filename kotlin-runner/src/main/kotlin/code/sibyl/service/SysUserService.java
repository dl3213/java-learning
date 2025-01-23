package code.sibyl.service;

import code.sibyl.common.r;
import code.sibyl.domain.base.BaseFile;
import code.sibyl.domain.sys.User;
import code.sibyl.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 系统用户 Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysUserService {

    private final SysUserRepository sysUserRepository;
    private final DatabaseClient databaseClient;

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public static SysUserService getBean() {
        return r.getBean(SysUserService.class);
    }

    public Mono<User> me() {
        Long id = 1L;
        return r2dbcEntityTemplate.selectOne(Query.query(Criteria.where("id").is(id)), User.class)
                .switchIfEmpty(Mono.just(new User(1L, "admin", "admin")))
                ;
    }

    public Mono<User> save(User user) {
        return sysUserRepository.save(user);
    }

    public Flux<User> list() {
        return sysUserRepository.list();
    }

}
