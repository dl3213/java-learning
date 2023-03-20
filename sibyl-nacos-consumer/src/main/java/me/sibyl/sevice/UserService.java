package me.sibyl.sevice;

import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname UserService
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 20:36
 */

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public User queryById(String id){
        return userMapper.selectById(id);
    }
}
