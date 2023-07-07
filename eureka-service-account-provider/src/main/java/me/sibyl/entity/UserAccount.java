package me.sibyl.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @Classname BusinessOrder
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/09 21:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName("user_account")
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class UserAccount extends BaseEntity {

    private String id;
    private Long userId;
    private String balance;
    private String state;
    @Version
    private Integer version;


}
