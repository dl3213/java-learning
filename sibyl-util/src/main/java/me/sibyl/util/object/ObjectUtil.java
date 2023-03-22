package me.sibyl.util.object;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * @Classname ObjectUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/18 18:48
 */

public final class ObjectUtil {

    public static boolean classHasParam(Class clazz, String paramName) {
        if (Objects.isNull(clazz) || StringUtils.isBlank(paramName)) {
            return false;
        }
        try {
            Field field = clazz.getDeclaredField(paramName);
            return Objects.nonNull(field);
        }catch (Exception e){
            return false;
        }
    }
}
