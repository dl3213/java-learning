package me.sibyl.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.sibyl.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @Classname UserMapper
 * @Description TODO
 * @Date 2021/7/27 20:42
 * @author dyingleaf3213
 */
@Repository
public interface UserMapper extends BaseMapper<User> {
}
