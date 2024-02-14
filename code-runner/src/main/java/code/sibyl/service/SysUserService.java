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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 *  系统用户 Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SysUserService implements ReactiveUserDetailsService {

    private final SysUserRepository sysUserRepository;

    public Mono<SysUser> save(SysUser sysUser) {
//        System.err.println("service.save => " + sysUser);
        return sysUserRepository.save(sysUser);
    }

    public Flux<SysUser> list() {
        return sysUserRepository.list();
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        System.err.println("findByUsername");
        System.err.println(username);
        //check todo
        return sysUserRepository.selectByUsername(username).map(u->{
            System.err.println("UserDetails builder");
            System.err.println(u);
            LoginUser user = new LoginUser();
            BeanUtils.copyProperties(u,user);
            user.setAuthorityList(AuthorityUtils.createAuthorityList("admin:api","user:api"));
            return user;
        });
    }
}
