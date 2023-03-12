package me.sibyl.service.impl;

import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import me.sibyl.service.UserQueryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Classname ServiceProvider2Impl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 06:25
 */
@Service
public class UserQueryServiceImpl implements UserQueryService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User queryDetail(String id){
        User user = userMapper.selectById(id);
        return user;
    }
}
