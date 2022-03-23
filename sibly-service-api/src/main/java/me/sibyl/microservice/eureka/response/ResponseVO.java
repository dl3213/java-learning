package me.sibyl.microservice.eureka.response;

import lombok.Data;

/**
 * @Classname Response
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/23 21:33
 */
@Data
public class ResponseVO {
    private Integer Code = 200;
    private String msg;
    private Object data;
}
