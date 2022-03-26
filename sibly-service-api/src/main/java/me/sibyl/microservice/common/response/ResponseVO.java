package me.sibyl.microservice.common.response;

import lombok.Data;
import lombok.ToString;

/**
 * @Classname Response
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/23 21:33
 */
@Data
@ToString
public class ResponseVO {
    private Integer Code = 200;
    private String msg;
    private Object data;
}
