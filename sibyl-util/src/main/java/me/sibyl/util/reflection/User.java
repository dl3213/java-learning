package me.sibyl.util.reflection;


import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

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
public class User {
    private String id;
    private String username;
    private String name;
    private String password;
    private Integer age;
    private String email;
    private String phone;
    private Integer status;
}
