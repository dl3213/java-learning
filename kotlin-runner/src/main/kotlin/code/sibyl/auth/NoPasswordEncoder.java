package code.sibyl.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码不加密匹配
 */

@Slf4j
public class NoPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence rawPassword) {
//        System.err.println("encode => " + rawPassword.toString());
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
//        System.err.println("matches: rawPassword => " + rawPassword.toString() + "; encodedPassword => " + encodedPassword);
        return rawPassword.toString().equals(encodedPassword);
    }
}
