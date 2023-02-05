package me.sibyl.cas.vo.request;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Classname LoginRequest
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/05 20:48
 */
@Data
@ToString
@Accessors(chain = true)
public class LoginRequest {
    private String username;
    private String password;
}
