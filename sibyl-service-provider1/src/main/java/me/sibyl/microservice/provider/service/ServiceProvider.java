package me.sibyl.microservice.provider.service;


import me.sibyl.microservice.common.request.RequestVO;
import me.sibyl.microservice.common.response.ResponseVO;

public interface ServiceProvider {
    ResponseVO test(RequestVO requestVO);
}
