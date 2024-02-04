package me.sibyl.dao;

import me.sibyl.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @Classname UserMapper
 * @Description TODO
 * @Date 2021/7/27 20:42
 * @Created by dyingleaf3213
 */
@Repository
@Mapper
public interface UserMapper extends BaseMapper<User> {

    public User queryById(Long id);

    @Select({"select * from t_sys_user where username = #{username} limit 1 "})
    User selectByUsername(@Param("username") String username);
}
