package me.sibyl.microservice.provider.nacos;

import me.sibyl.common.response.Response;
import me.sibyl.microservice.request.AccountConsumeRequest;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author dyingleaf3213
 * @Classname BubboAccountService
 * @Description TODO
 * @Create 2023/03/19 20:18
 */

public interface DubboAccountService {

    String consume(AccountConsumeRequest requestVO);
}
