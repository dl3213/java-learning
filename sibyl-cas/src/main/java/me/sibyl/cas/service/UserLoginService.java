package me.sibyl.cas.service;


import me.sibyl.base.entity.User;
import me.sibyl.common.response.ResponseVO;

public interface UserLoginService {

    ResponseVO login(User user);

    ResponseVO logout();
}
