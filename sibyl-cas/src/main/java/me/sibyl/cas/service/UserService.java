package me.sibyl.cas.service;


import com.baomidou.mybatisplus.extension.service.IService;
import me.sibyl.base.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    List<User> listAll();

    User detail(String id);

    void put(User user);
}
