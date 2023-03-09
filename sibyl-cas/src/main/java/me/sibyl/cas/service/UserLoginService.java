package me.sibyl.cas.service;


import me.sibyl.cas.vo.request.LoginRequest;
import me.sibyl.common.response.Response;

public interface UserLoginService {

    Response login(LoginRequest user);

    Response logout();
}
