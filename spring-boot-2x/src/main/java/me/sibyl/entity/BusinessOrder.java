package me.sibyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("business_order")
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class BusinessOrder extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private String amount;
    private String orderState;
    private String linkId;


}
