package me.sibyl.aspect;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.lang.annotation.Annotation;

/**
 * @Classname EasyField
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/18 21:44
 */
@Data
@ToString
@Builder
public final class EasyField {

    private String name;
    private Class clazz;
    private Object value;
    private Integer index;
    private Annotation[] declaredAnnotationList;
}
