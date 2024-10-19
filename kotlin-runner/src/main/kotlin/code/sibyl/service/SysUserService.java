package code.sibyl.service;

import code.sibyl.domain.sys.User;
import code.sibyl.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public Mono<User> save(User user) {
        return sysUserRepository.save(user);
    }

    public Flux<User> list() {
        return sysUserRepository.list();
    }

}
