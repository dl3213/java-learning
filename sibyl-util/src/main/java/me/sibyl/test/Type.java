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
public enum Type implements EnumInterface {

    type1("1", "type1"),
    type2("2", "type2"),
    ;

    private String code;
    private String value;
}
