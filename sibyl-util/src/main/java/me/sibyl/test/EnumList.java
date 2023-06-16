package me.sibyl.test;

import java.lang.annotation.*;

/**
 * @Classname EnumList
 * @Description EnumList
 * @Date 2023/6/16 11:27
 * @Author by Qin Yazhi
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE})
@Documented
public @interface EnumList {
}
