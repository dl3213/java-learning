package me.sibyl.microservice.provider.eureka;

import me.sibyl.common.response.Response;
import me.sibyl.common.response.ResponseVO;
import me.sibyl.microservice.request.AccountConsumeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Classname ProviderFeign
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/22 22:13
 */
@Component
@FeignClient(value = "sibyl-eureka-service-account-provider", path = "/api/v1/account")
public interface AccountServiceProvider {

    @RequestMapping(value = "/consume", method = {RequestMethod.POST})
    Response consume(@RequestBody AccountConsumeRequest requestVO);
}
