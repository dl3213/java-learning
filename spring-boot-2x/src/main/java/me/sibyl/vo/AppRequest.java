package me.sibyl.vo;

import lombok.Data;
import me.sibyl.annotation.Watching;

/**
 * @Classname AppRequest
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/18 18:01
 */
@Data
public class AppRequest {
    private String id;
    @Watching
    private String value;
    @Watching
    private int get;
}
