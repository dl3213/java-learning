package me.sibyl.test;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Classname Type
 * @Description Type
 * @Date 2023/6/16 11:24
 * @Author by Qin Yazhi
 */
@EnumList
@AllArgsConstructor
@Getter
public enum Type {

    type1(1, "type1"),
    type2(2, "type2"),
    ;

    private int key;
    private String value;
}
