package me.sibyl.aspect;

import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import me.sibyl.annotation.Watching;
import me.sibyl.common.config.SibylException;
import me.sibyl.util.RequestUtils;
import me.sibyl.util.object.ObjectUtil;
import me.sibyl.vo.AppRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Classname AopUtil
 * @Description TODO
 * @Author dyingleaf3213
 * @Create 2023/03/18 19:40
 */
@Slf4j
public class AopJoinPointUtil {

    public static String getCacheKeyByTarget(String keyPrefix, JoinPoint joinPoint, TargetMode mode, Class[] classes, String[] paramNames) {
        String noRepeatSubmitKey = keyPrefix;

        mode = Objects.nonNull(mode) ? mode : TargetMode.session;

        switch (mode) {
            case session:
                noRepeatSubmitKey = builderBySession(noRepeatSubmitKey);
                break;
            case classParam:
                noRepeatSubmitKey = builderByClassParamValue(joinPoint, classes, paramNames, noRepeatSubmitKey);
                break;
            case watching:// todo 只能获取方法的第一层参数和第二层参数(参数的参数)
                noRepeatSubmitKey = builderByWatching(joinPoint, noRepeatSubmitKey);
                break;
            default:
                noRepeatSubmitKey = builderBySession(noRepeatSubmitKey);
                break;
        }
        return noRepeatSubmitKey;
    }

    private static String builderByClassParamValue(JoinPoint joinPoint, Class[] classes, String[] paramNames, String noRepeatSubmitKey) {
        if (!validatedWatcher(classes, paramNames)) throw new SibylException("TargetMode = classParam validated fail ");
        // class[param=value;]-
        String classParamValueBuilder = Arrays.stream(classes)
                .filter(Objects::nonNull)
                .map(watchClass -> classValueBuilder(joinPoint, paramNames, watchClass))
                .collect(Collectors.joining("-"));
        noRepeatSubmitKey += classParamValueBuilder;
        return noRepeatSubmitKey;
    }

    private static String builderByWatching(JoinPoint joinPoint, String noRepeatSubmitKey) {
        CodeSignature signature = (CodeSignature) joinPoint.getSignature();
        String[] paramArr = signature.getParameterNames();// 这些数组的数量相同
        //参数类型 此时不知道参数是否有注解
        Class[] typeArr = signature.getParameterTypes();// 这些数组的数量相同
        //参数值
        Object[] args = joinPoint.getArgs();// 这些数组的数量相同
        //
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        Parameter[] parameters = targetMethod.getParameters();// 这些数组的数量相同

        //先构造方式所有参数列表
        LinkedList<EasyField> fileList = new LinkedList<>();
        for (int i = 0; i < args.length; i++) {
            EasyField easyField = EasyField.builder()
                    .name(paramArr[i])
                    .clazz(typeArr[i])
                    .value(args[i])
                    .index(i)
                    .declaredAnnotationList(parameters[i].getDeclaredAnnotations())
                    .build();
            fileList.add(easyField);
        }
        // todo 选出被Watching的参数，拼接成：名称=值， 多个以-连接
        String collect = fileList
                .stream()
                .filter(Objects::nonNull)
                .filter(easyField -> Arrays.stream(easyField.getDeclaredAnnotationList()).anyMatch(e -> e.annotationType().equals(Watching.class)))
                .map(easyField -> easyField.getName() + "=" + String.valueOf(easyField.getValue()))
                .collect(Collectors.joining("-"));
        noRepeatSubmitKey += collect;
        return noRepeatSubmitKey;
    }

    private static String builderBySession(String noRepeatSubmitKey) {
        HttpServletRequest request = RequestUtils.getRequest();
        String sessionId = RequestUtils.getServletRequestAttributes().getSessionId();
        noRepeatSubmitKey += sessionId + "-" + request.getServletPath();
        return noRepeatSubmitKey;
    }

    public static void main(String[] args) throws Exception {
        String str = "test";
        int value = 1;
        AppRequest request = new AppRequest();
        request.setId("dl3213");

        //y
        Gson gson = new GsonBuilder().create();
//        System.err.println(gson.fromJson());
        System.err.println(gson.toJson(str));
        System.err.println(gson.toJson(value));
        System.err.println(gson.toJson(request));
    }

    public static String classValueBuilder(JoinPoint joinPoint, String[] paramNames, Class<?> watchClass) {
        StringBuffer stringBuffer = new StringBuffer();
        String keyBuilder = classParamValueBuilder(joinPoint, paramNames, watchClass);
        if (StringUtils.isBlank(keyBuilder)) return null;
        stringBuffer.append(keyBuilder);
        return stringBuffer.toString();
    }

    public static String classParamValueBuilder(JoinPoint joinPoint, String[] paramNames, Class<?> watchClass) {
        if (Objects.isNull(watchClass)) return null;
        StringBuffer classValueBuilder = new StringBuffer();
        classValueBuilder.append(watchClass.getName());
        classValueBuilder.append("[");

        String collect = Arrays.stream(paramNames)
                .filter(StringUtils::isNotBlank)
                .map(paramName -> paramValueBuilder(joinPoint, watchClass, paramName))
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.joining(";"));
        classValueBuilder.append(collect);
        classValueBuilder.append("]");
        return classValueBuilder.toString();
    }

    public static String paramValueBuilder(JoinPoint joinPoint, Class<?> watchClass, String paramName) {
        if (!ObjectUtil.classHasParam(watchClass, paramName)) return null;
        Object arg = Arrays.stream(joinPoint.getArgs()).filter(Objects::nonNull).filter(e -> watchClass.getName().equals(e.getClass().getName())).findFirst().orElse(null);
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
