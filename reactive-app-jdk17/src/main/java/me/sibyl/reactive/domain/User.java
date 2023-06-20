package me.sibyl.reactive.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

/**
 * @Classname User
 * @Description TODO
 * @Date 2021/7/27 22:31
 * @Created by dyingleaf3213
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table("sys_user")
public class User {
    @Id
    private String id;
    private String username;
    private String name;
    private String password;
    private Integer age;
    private String email;
    private String phone;
    private Integer status;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
    }
}
