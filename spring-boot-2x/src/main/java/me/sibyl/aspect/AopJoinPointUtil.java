package me.sibyl.aspect;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.annotation.NoRepeatSubmit;
import me.sibyl.util.RequestUtils;
import me.sibyl.util.object.ObjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Classname AopUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/18 19:40
 */
@Slf4j
public class AopJoinPointUtil {

    public static String getCacheKeyByTarget(String keyPrefix, ProceedingJoinPoint pjp, Class[] classes, String[] paramNames) {
        String noRepeatSubmitKey = keyPrefix;

        if (validatedWatcher(classes, paramNames)) {
            // class[param=value]-
            String classParamValueBuilder = Arrays.stream(classes)
                    .filter(Objects::nonNull)
                    .map(watchClass -> classValueBuilder(pjp, paramNames, watchClass))
                    .collect(Collectors.joining("-"));
            noRepeatSubmitKey += classParamValueBuilder;
        } else {
            HttpServletRequest request = RequestUtils.getRequest();
            String sessionId = RequestUtils.getServletRequestAttributes().getSessionId();
            noRepeatSubmitKey += sessionId + "-" + request.getServletPath();
        }
        return noRepeatSubmitKey;
    }


    public static String classValueBuilder(ProceedingJoinPoint pjp, String[] paramNames, Class<?> watchClass) {
        StringBuffer stringBuffer = new StringBuffer();
        String keyBuilder = classParamValueBuilder(pjp, paramNames, watchClass);
        if (StringUtils.isBlank(keyBuilder)) return null;
        stringBuffer.append(keyBuilder);
        return stringBuffer.toString();
    }

    public static String classParamValueBuilder(ProceedingJoinPoint pjp, String[] paramNames, Class<?> watchClass) {
        if (Objects.isNull(watchClass)) return null;
        StringBuffer classValueBuilder = new StringBuffer();
        classValueBuilder.append(watchClass.getName());
        classValueBuilder.append("[");

        String collect = Arrays.stream(paramNames)
                .filter(StringUtils::isNotBlank)
                .map(paramName -> paramValueBuilder(pjp, watchClass, paramName))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(";"));
        classValueBuilder.append(collect);
        classValueBuilder.append("]");
        return classValueBuilder.toString();
    }

    public static String paramValueBuilder(ProceedingJoinPoint pjp, Class<?> watchClass, String paramName) {
        if (!ObjectUtil.classHasParam(watchClass, paramName)) return null;
        Object arg = Arrays.stream(pjp.getArgs()).filter(Objects::nonNull).filter(e -> watchClass.getName().equals(e.getClass().getName())).findFirst().orElse(null);
        if (Objects.isNull(arg)) return null;
        JSONObject obj = JSONObject.from(arg);
        String stringValue = obj.getString(paramName);
        if (StringUtils.isBlank(stringValue)) return null;
        return paramName + "=" + stringValue;
    }

    public static boolean validatedWatcher(Class[] classes, String[] paramNames) {
        return classes != null && classes.length > 0 && paramNames != null && paramNames.length > 0;
    }
}
