package me.sibyl.annotation;

import java.lang.annotation.*;

/**
 * @Classname NoRepeatSubmit
 * @Description 时间内请求次数限制
 * @Date 2022/3/29 14:09
 * @Author by Qin Yazhi
 */
@Documented
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RequestCountLimit {

    /**
     * 限制时间 单位：秒
     */
    int time() default 15;

    /**
     * 允许请求的次数
     */
    int count() default 3;

    /**
     *
     */
    Class<?>[] watchClass() default {};

    /**
     *
     */
    String[] classParamName() default {};
}
