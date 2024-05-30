package code.sibyl.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RentRecycleDTO {

    private String sales_contract;
    private String material_code;
    private String back_num;
    private String ret;
}
