package me.sibyl.base.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @Classname RolePermission
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/02/26 00:38
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName("sys_role_permission")
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class RolePermission {
    private String id;
    private String roleId;
    private String pId;
}
