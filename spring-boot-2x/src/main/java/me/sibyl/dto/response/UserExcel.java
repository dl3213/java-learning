package me.sibyl.dto.response;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

@Data
public class UserExcel {

    @ExcelProperty("username")
    private String username;
    @ExcelProperty("name")
    private String name;
    @ExcelProperty("password")
    private String password;
    @ExcelProperty("age")
    private Integer age;
    @ExcelProperty("email")
    private String email;
    @ExcelProperty("phone")
    private String phone;
    @ExcelProperty("status")
    private Integer status;
}
