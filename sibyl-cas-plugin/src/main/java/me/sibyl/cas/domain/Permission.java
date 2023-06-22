package me.sibyl.cas.domain;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

/**
 * @Classname User
 * @Description TODO
 * @Date 2021/7/27 22:31
 * @Created by dyingleaf3213
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class Permission implements Serializable {


    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String code;
//    private Integer status;
//    private String path;
//    private String component;
}
