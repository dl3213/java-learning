package me.sibyl.auth;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @return 结果
     */
    public LoginUser login(String username, String password) {
        String token = null;
        LoginUser loginUser = null;
        // 用户验证
        Authentication authentication = null;
        // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
        authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        loginUser = (LoginUser) authentication.getPrincipal();
        token = jwtTokenUtil.generateToken(loginUser);
        loginUser.setToken(token);
        return loginUser;
    }
}