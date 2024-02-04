package me.sibyl.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserMapper userMapper;

    /**
     * 登录信息验证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User sysUser = userMapper.selectByUsername(username);
        if (Objects.nonNull(sysUser))
        {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录账号不存在");
        }
//        else if (PublicEnum.DELETE.getCode().equals(sysUser.getStatus()))
//        {
//            log.info("登录用户：{} 已被删除.", username);
//            throw new CustomException("账号已被删除");
//        }
//        else if (PublicEnum.DISABLE.getCode().equals(sysUser.getStatus()))
//        {
//            log.info("登录用户：{} 已被停用.", username);
//            throw new CustomException("账号已停用");
//        }
        return createLoginUser(sysUser);
    }

    public UserDetails createLoginUser(User user)
    {
        return new LoginUser(user, null);
    }
}
