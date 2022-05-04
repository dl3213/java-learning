package me.sibyl.cas.expression;

import me.sibyl.cas.domain.LoginUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Classname SibylExpressionRoot
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 16:41
 */
@Component("exp")
public class SibylExpressionRoot {

    public boolean hasAuth(String auth){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<String> permissions = loginUser.getPermissions();
        return permissions.contains(auth);
    }
}
