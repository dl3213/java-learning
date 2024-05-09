package code.sibyl.service;

import code.sibyl.model.LoginUser;
import code.sibyl.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * 系统用户 Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginUserService implements ReactiveUserDetailsService {

    private final SysUserRepository sysUserRepository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        //check todo
        return sysUserRepository.selectByUsername(username).map(u->{
            LoginUser user = new LoginUser();
            BeanUtils.copyProperties(u,user);
            user.setAuthorityList(AuthorityUtils.createAuthorityList("admin:api","user:api"));
            return user;
        });
    }
}
