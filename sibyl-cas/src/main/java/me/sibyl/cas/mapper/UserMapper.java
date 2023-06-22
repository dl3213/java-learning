package me.sibyl.cas.mapper;

import me.sibyl.cas.domain.Permission;
import me.sibyl.cas.domain.Role;
import me.sibyl.cas.domain.User;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname UserMapper
 * @Description TODO
 * @Date 2021/7/27 20:42
 * @Created by dyingleaf3213
 */
@Repository
public interface UserMapper {
    User queryByUsername(@Param("username") String username);

    List<Role> queryUserRoles(@Param("uid")String uid);

    List<Permission> queryUserRolePermissions(@Param("uid") String uid);
}
