package me.sibyl.annotation;

import java.lang.annotation.*;

/**
 * @Classname SqlLogging
 * @Description SqlLogging
 * @Date 2023/4/19 15:49
 * @Author by Qin Yazhi
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SqlLogging {
}
