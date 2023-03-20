package me.sibyl.annotation;

import java.lang.annotation.*;

/**
 * @Classname NoRepeatSubmit
 * @Description 重复请求注解
 * @Date 2022/3/29 14:09
 * @Author by Qin Yazhi
 */

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Watching {

}
