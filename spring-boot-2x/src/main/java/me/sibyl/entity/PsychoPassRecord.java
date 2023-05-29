package me.sibyl.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

/**
 * @Classname PsychoPassRecord
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/08 21:00
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@TableName("psycho_pass_record_202305")
@EqualsAndHashCode
@Accessors(chain = true)
@ToString(callSuper = true)
public class PsychoPassRecord extends BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private String uid;
    private String psychoPass;
    private String type;
    private String code;
    private String flag;
    private String state;
}
