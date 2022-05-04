package me.sibyl.cas.base.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import me.sibyl.cas.base.BaseEntity;
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
@TableName("role")
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class Role extends BaseEntity {
    @TableId
    private Long id;
    private String name;
    private String key;
}
