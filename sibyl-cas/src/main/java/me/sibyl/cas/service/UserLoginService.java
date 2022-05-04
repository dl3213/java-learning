package me.sibyl.cas.service;

import me.sibyl.cas.base.domain.R;
import me.sibyl.cas.base.entity.User;

public interface UserLoginService {

    R login(User user);
    R logout();
}
