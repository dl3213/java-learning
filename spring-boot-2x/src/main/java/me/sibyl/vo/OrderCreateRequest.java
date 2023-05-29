package me.sibyl.vo;

import lombok.Data;
import me.sibyl.annotation.Watching;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;

/**
 * @Classname OrderCreateRequest
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/09 21:12
 */
@Data
public class OrderCreateRequest {
    @NonNull
    private BigDecimal amount;

    @Watching
    private String linkId;
}
