package me.sibyl.microservice.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Classname OrderCreateRequest
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/09 21:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest implements Serializable {

    private static final long serialVersionUID = -1L;

    @NonNull
    private BigDecimal amount;
    private String linkId;
}
