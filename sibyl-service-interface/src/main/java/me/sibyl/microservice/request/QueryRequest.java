package me.sibyl.microservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
public class QueryRequest {
    private String id;
    private String username;
    private String value;
}
