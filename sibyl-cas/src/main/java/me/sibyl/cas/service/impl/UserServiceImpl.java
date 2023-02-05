package me.sibyl.cas.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.sibyl.base.entity.User;
import me.sibyl.cas.mapper.UserMapper;
import me.sibyl.cas.service.UserService;
import me.sibyl.common.config.SibylException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Classname UserServiceImpl
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/01/22 22:48
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public List<User> listAll() {
        return this.getBaseMapper().selectList(null);
    }

    @Override
    public User detail(String id) {
//        throw new SibylException("test");
        return this.getBaseMapper().selectById(id);
    }

    @Override
    public void put(User user) {
        this.saveOrUpdate(user);
    }
}
