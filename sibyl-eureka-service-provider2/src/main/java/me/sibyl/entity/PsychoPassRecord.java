package me.sibyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * @Classname PychoPassRecord
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/08 21:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName("psycho_pass_record")
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class PsychoPassRecord extends BaseEntity {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    private String uid;
    private String psychoPass;
    private String type;
}
