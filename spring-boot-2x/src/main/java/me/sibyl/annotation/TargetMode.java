package me.sibyl.annotation;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TargetMode {

    session(1),//根据session
    classParam(2),//方式参数,watching已兼容
    watching(3),//指定参数
    ;

    public int code;
}
