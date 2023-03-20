package me.sibyl.annotation;

import me.sibyl.aspect.TargetMode;

import java.lang.annotation.*;

/**
 * @Classname NoRepeatSubmit
 * @Description 重复请求注解
 * @Date 2022/3/29 14:09
 * @Author by Qin Yazhi
 */

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NoRepeatBeforeSubmit {
    /**
     *  过期时间 秒
     **/
    long expire() default 10;

    /**
     *  指定方法的参数类
     */
    Class<?>[] watchClass() default {};

    /**
     * 指定方法的参数字段
     */
    String[] classParamName() default {};

    /**
     *  指定类型
     */
    TargetMode mode() default TargetMode.session;
}
