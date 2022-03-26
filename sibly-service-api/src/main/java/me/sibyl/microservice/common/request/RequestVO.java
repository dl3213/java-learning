package me.sibyl.microservice.common.request;

import lombok.Data;
import lombok.ToString;

/**
 * @Classname RequestVO
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/23 21:32
 */
@Data
@ToString
public class RequestVO {
    private Long id;
    private String name;
}
