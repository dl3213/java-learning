package me.sibyl.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("sys_user")
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class User extends BaseEntity {
    @TableId
    private String id;
    private String username;
    private String name;
    private String password;
    private Integer age;
    private String email;
    private String phone;
    private Integer status;
}
