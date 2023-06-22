package me.sibyl.cas.config;

import me.sibyl.cas.util.MD5Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Classname MyPasswordEncoder
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 11:09
 */

public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encodedPassword.equals(rawPassword);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return MD5Util.encode(String.valueOf(rawPassword));
    }
}
