package me.sibyl.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.sibyl.dao.UserMapper;
import me.sibyl.entity.User;
import me.sibyl.service.UserService;
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
        User user = this.getBaseMapper().queryById(Long.valueOf(id));
        return user;
    }
}
