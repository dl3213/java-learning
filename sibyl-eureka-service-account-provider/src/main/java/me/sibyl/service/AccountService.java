package me.sibyl.service;


import me.sibyl.microservice.request.AccountConsumeRequest;
import me.sibyl.microservice.request.OrderCreateRequest;

/**
 * @Classname ServiceProvider2
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 06:24
 */

public interface AccountService {

    String consume(AccountConsumeRequest orderCreateRequest);
}
