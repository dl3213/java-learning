package code.sibyl.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TestDTO {

    private String sales_contract;
    private String project_name;
    private String org_code;
    private String org_name;
    private String cust_code;
    private String cust_name;
    private String region;
    private String region_name;
    private String delivery_warehouse;
    private String store_name;
    private String region_company;
    private String region_company_name;
    private String material_code;
    private String material_name;
    private String specification_and_model;
    private String unit_ton_weight;
    private String settlement_ton_weight;
    private String primary_quantity;
    private String back_num;
    private String ret;
}
