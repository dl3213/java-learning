package me.sibyl.vo;

import lombok.Data;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

/**
 * @Classname ConsumeRequest
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/12 22:05
 */
@Data
public class ConsumeRequest {
    @NonNull
    private Long userId;
    @NonNull
    private BigDecimal amount;
}
