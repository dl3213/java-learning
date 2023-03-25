package me.sibyl.microservice.provider.eureka;

import lombok.extern.slf4j.Slf4j;
import me.sibyl.common.response.Response;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * @author dyingleaf3213
 * @Classname AccountConsumeFallbackFactoryFactory
 * @Description TODO
 * @Create 2023/03/26 05:16
 */
@Component
@Slf4j
public class AccountConsumeFallbackFactoryFactory implements FallbackFactory<AccountServiceProvider> {
    @Override
    public AccountServiceProvider create(Throwable cause) {
        cause.printStackTrace();
        return requestVO -> Response.error("AccountConsumeFallbackFactoryFactory");
    }
}
