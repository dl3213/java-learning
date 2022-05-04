package me.sibyl.cas.service;

import me.sibyl.base.domain.ResponseVO;
import me.sibyl.base.entity.User;

public interface UserLoginService {

    ResponseVO login(User user);

    ResponseVO logout();
}
