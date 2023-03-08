package me.sibyl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.sibyl.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    List<User> listAll();

    User detail(String id);
}
