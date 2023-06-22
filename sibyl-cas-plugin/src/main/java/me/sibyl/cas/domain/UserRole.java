package me.sibyl.cas.domain;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

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
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class UserRole implements Serializable {
    private String userId;
    private String roleId;
}
