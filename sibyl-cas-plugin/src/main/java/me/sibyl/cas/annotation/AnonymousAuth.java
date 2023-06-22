package me.sibyl.cas.annotation;

import java.lang.annotation.*;

/**
 * @author dyingleaf3213
 * @Classname Anonymous
 * @Description TODO
 * @Create 2023/06/23 03:25
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AnonymousAuth {
}
