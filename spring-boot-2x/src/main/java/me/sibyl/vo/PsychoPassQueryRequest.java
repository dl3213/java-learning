package me.sibyl.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author dyingleaf3213
 * @Classname PsychoPassQueryRequest
 * @Description TODO
 * @Create 2023/04/11 22:01
 */
@Data
@ToString
public class PsychoPassQueryRequest {

    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private String uid;
}
