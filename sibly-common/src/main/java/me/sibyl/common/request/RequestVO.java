package me.sibyl.common.request;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Classname RequestVO
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/23 21:32
 */
@Data
@ToString
@Accessors(chain = true)
public class RequestVO {
    private Long id;
    private String name;
}
