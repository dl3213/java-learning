package me.sibyl.microservice.provider.nacos;

import me.sibyl.microservice.request.AccountConsumeRequest;

/**
 * @author dyingleaf3213
 * @Classname BubboAccountService
 * @Description TODO
 * @Create 2023/03/19 20:18
 */

public interface DubboAccountService {

    Long consume(AccountConsumeRequest requestVO);
}
