package me.sibyl.util;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.*;

/**
 * @author dyingleaf3213
 * @Classname SqlUtils
 * @Description TODO
 * @Create 2023/04/19 20:16
 */
public class SqlUtils {

    /**
     * 获取aop中的SQL语句
     *
     * @param joinPoint
     * @param sqlSessionFactory
     * @return
     * @throws IllegalAccessException
     */
    public static String getMybatisSql(JoinPoint joinPoint, SqlSessionFactory sqlSessionFactory) throws IllegalAccessException {
        System.err.println("getMybatisSql ==> ");
        //1.获取namespace+methdoName
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();
        //在mybatis-plusxia,非自定义sql就获取到com.baomidou.mybatisplus.core.mapper.BaseMapper
        String namespace = targetClass.getName();
        String methodName = method.getName();
        String id = namespace + "." + methodName;
        // 若不是自定义sql,就返回类名+方法
        if(targetClass.getName().equals(com.baomidou.mybatisplus.core.mapper.BaseMapper.class.getName())){
            return id;
        }
        //2.根据namespace+methdoName获取相对应的MappedStatement
        Configuration configuration = sqlSessionFactory.getConfiguration();

//        Collection<String> mappedStatementNames = configuration.getMappedStatementNames();
//        mappedStatementNames.forEach(System.err::println);
//        System.err.println();

        MappedStatement mappedStatement = configuration.getMappedStatement(id);
//        //3.获取方法参数列表名
//        Parameter[] parameters = method.getParameters();
        //4.形参和实参的映射
        Object[] objects = joinPoint.getArgs(); //获取实参
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        Map<String, Object> map = new HashMap<>();
//        for (int i = 0; i < parameterAnnotations.length; i++) {
//            Object object = objects[i];
//            if (parameterAnnotations[i].length == 0) { //说明该参数没有注解，此时该参数可能是实体类，也可能是Map，也可能只是单参数
//                if (object.getClass().getClassLoader() == null && object instanceof Map) {
//                    map.putAll((Map<? extends String, ?>) object);
//                    System.out.println("该对象为Map");
//                } else {//形参为自定义实体类
//                    map.putAll(objectToMap(object));
//                    System.out.println("该对象为用户自定义的对象");
//                }
//            } else {//说明该参数有注解，且必须为@Param
//                for (Annotation annotation : parameterAnnotations[i]) {
//                    if (annotation instanceof Param) {
//                        map.put(((Param) annotation).value(), object);
//                    }
//                }
//            }
//        }
        //5.获取boundSql
        BoundSql boundSql = mappedStatement.getBoundSql(map);
        //return showSql(configuration, boundSql);
        return boundSql.getSql();
    }

    /**
     * 解析BoundSql，生成不含占位符的SQL语句
     *
     * @param configuration
     * @param boundSql
     * @return
     */
    private static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
//        if (parameterMappings.size() > 0 && parameterObject != null) {
//            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
//            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
//                sql = sql.replaceFirst("\\?", getParameterValue(parameterObject));
//            } else {
//                MetaObject metaObject = configuration.newMetaObject(parameterObject);
//                for (ParameterMapping parameterMapping : parameterMappings) {
//                    String propertyName = parameterMapping.getProperty();
//                    String[] s = metaObject.getObjectWrapper().getGetterNames();
//                    s.toString();
//                    if (metaObject.hasGetter(propertyName)) {
//                        Object obj = metaObject.getValue(propertyName);
//                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
//                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
//                        Object obj = boundSql.getAdditionalParameter(propertyName);
//                        sql = sql.replaceFirst("\\?", getParameterValue(obj));
//                    }
//                }
//            }
//        }
        return sql;
    }

    /**
     * 若为字符串或者日期类型，则在参数两边添加''
     *
     * @param obj
     * @return
     */
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    private static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        System.out.println(clazz);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = field.get(obj);
            map.put(fieldName, value);
        }
        return map;
    }
}
