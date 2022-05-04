package me.sibyl.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Classname Response
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2022/03/23 21:33
 */
@Data
@ToString
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO {
    private Integer code = 200;
    private String msg;
    private Object data;

    public ResponseVO(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResponseVO(Integer code, Object data) {
        this.code = code;
        this.data = data;
    }

    public ResponseVO(Object data) {
        this.code = 200;
        this.msg = "操作成功";
        this.data = data;
    }
}
