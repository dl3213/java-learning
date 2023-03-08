package me.sibyl.cas.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.sibyl.base.entity.Permission;
import me.sibyl.base.entity.Role;
import me.sibyl.base.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Classname LoginUser
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/02/26 10:46
 */
// 注解 避免JSON返回不必要的空数据
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityUser implements UserDetails {
    /**
     * 当前登录用户
     */
    private User currentUserInfo;

    /**
     * 角色
     */
    private List<Role> roleList;

    /**
     * 用户权限值
     */
    private List<Permission> permissionList;


    private String token;


    /**
     * 空参构造
     */
    public SecurityUser() {
    }

    /**
     * 用户信息构造
     *
     * @param user
     */
    public SecurityUser(User user) {
        if (user != null) {
            this.currentUserInfo = user;
        }
    }

    public SecurityUser(User user, List<Role> roleList) {
        if (user != null) {
            this.currentUserInfo = user;
            this.roleList = roleList;
        }
    }

    public SecurityUser(User user, List<Role> roleList, List<Permission> permissionList) {
        if (user != null) {
            this.currentUserInfo = user;
            this.roleList = roleList;
            this.permissionList = permissionList;
        }
    }


    @Override
    public String getPassword() {
        return currentUserInfo.getPassword();
    }

    @Override
    public String getUsername() {
        return currentUserInfo.getUsername();
    }

    /**
     * 用户是否过期
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 用户是否被不锁定
     *
     * @return true 用户被锁定  false用户没有被锁定
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 账户登录凭证是否过期
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 用户是否启用
     *
     * @return true是  false否
     */
    @Override
    public boolean isEnabled() {
        return true;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getCurrentUserInfo() {
        return currentUserInfo;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }


    /**
     * 获取当前用户具有的角色
     * 用户添加权限。返回权限对象
     * 这个方法很重要，会影响到后面的权限资源控制
     * @return GrantedAuthority
     */
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        // 判断权限列表是否为空
        if (!CollectionUtils.isEmpty(this.permissionList)) {
            // 对权限列表进行遍历
            for (Permission permission : this.permissionList) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permission.getCode());
                // 将权限添加到集合中
                authorities.add(authority);
            }
        }
        //返回权限
        return authorities;
    }
}
