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
public class AccountConsumeRequest implements Serializable {

    private static final long serialVersionUID = -1L;

    @NonNull
    private String userId;
    @NonNull
    private BigDecimal amount;

}
