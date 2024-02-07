package code.sibyl.service;

import code.sibyl.domain.SysUser;
import code.sibyl.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class SysUserService {

    private final SysUserRepository sysUserRepository;

    public Mono<SysUser> save(SysUser sysUser) {
//        System.err.println("service.save => " + sysUser);
        return sysUserRepository.save(sysUser);
    }

    public Flux<SysUser> list() {
        return sysUserRepository.list();
    }
}
