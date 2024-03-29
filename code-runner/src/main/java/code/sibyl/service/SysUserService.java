package code.sibyl.service;

import code.sibyl.domain.SysUser;
import code.sibyl.dto.LoginUser;
import code.sibyl.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  系统用户 Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysUserService
//        implements ReactiveUserDetailsService
{

    private final SysUserRepository sysUserRepository;

    public Mono<SysUser> save(SysUser sysUser) {
        return sysUserRepository.save(sysUser);
    }

    public Flux<SysUser> list() {
        return sysUserRepository.list();
    }

}
