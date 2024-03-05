package code.sibyl.service;

import code.sibyl.domain.SysUser;
import code.sibyl.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *  系统用户 Service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class LoginUserService
//        implements ReactiveUserDetailsService
{

//    @Override
//    public Mono<UserDetails> findByUsername(String username) {
//        //check todo
//        return sysUserRepository.selectByUsername(username).map(u->{
//            LoginUser user = new LoginUser();
//            BeanUtils.copyProperties(u,user);
//            user.setAuthorityList(AuthorityUtils.createAuthorityList("admin:api","user:api"));
//            return user;
//        });
//    }
}
