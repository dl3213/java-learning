package me.sibyl.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @Classname UserRole
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/26 00:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName("sys_user_role")
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class UserRole  {
    private String userId;
    private String roleId;
}
