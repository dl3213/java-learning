package me.sibyl.microservice.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @Classname Response
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/23 21:33
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO {
    private Integer Code = 200;
    private String msg;
    private Object data;

    public ResponseVO(Integer code, String msg) {
        Code = code;
        this.msg = msg;
    }
}
