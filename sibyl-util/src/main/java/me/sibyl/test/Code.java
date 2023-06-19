package me.sibyl.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author dyingleaf3213
 * @Classname Type
 * @Description TODO
 * @Create 2023/06/19 22:10
 */
@Getter
@AllArgsConstructor
public enum Code implements EnumInterface {

    code1("1", "code1"),
    code2("2", "code2"),
    ;

    private String code;
    private String value;
}
