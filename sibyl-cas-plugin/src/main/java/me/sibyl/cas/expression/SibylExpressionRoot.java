package me.sibyl.cas.expression;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.base.entity.Permission;
import me.sibyl.cas.domain.SecurityUser;
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
@Slf4j
public class SibylExpressionRoot {

    public boolean hasAuth(String auth) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        List<Permission> permissions = securityUser.getPermissionList();
        log.info("user={}, permissions={}", securityUser.getUsername(), permissions);
        return permissions.stream().anyMatch(e -> auth.equals(e.getCode()));
    }
}
