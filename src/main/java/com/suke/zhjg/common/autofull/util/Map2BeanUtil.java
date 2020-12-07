package com.suke.zhjg.common.autofull.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author czx
 * @title: Map2BeanUtil
 * @projectName zhjg
 * @description: TODO
 * @date 2020/9/98:44
 */
@Slf4j
@UtilityClass
public class Map2BeanUtil {

    public Object convert(Map map, Object object){
        if(map == null || object == null){
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = toLowerCaseFirstOne(property.getName());
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    setter.invoke(object, value);
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return object;
    }

    public <T> void setProperty(T clazz,String field,Object value,int level){
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = toLowerCaseFirstOne(property.getName());
                if (field.equals(key)) {
                    // 得到property对应的setter方法
                    Method setter = property.getWriteMethod();
                    Class<?>[] parameterTypes = setter.getParameterTypes();
                    // 如果bean 是String 类型 直接把value 转成String
                    if(parameterTypes[0].getName().equals("java.lang.String")){
                        setter.invoke(clazz, String.valueOf(value));
                    }else if(parameterTypes[0].getName().equals("java.lang.Long")) {
                        String s = String.valueOf(value);
                        if(s.contains(".")){
                            setter.invoke(clazz, Long.valueOf(s.substring(0,s.indexOf("."))));
                        }else {
                            setter.invoke(clazz, Long.valueOf(s));
                        }
                    }else if(parameterTypes[0].getName().equals("java.lang.Integer")) {
                        String s = String.valueOf(value);
                        if(s.contains(".")){
                            setter.invoke(clazz, Integer.valueOf(s.substring(0,s.indexOf("."))));
                        }else {
                            setter.invoke(clazz, Integer.valueOf(s));
                        }
                    }else if(parameterTypes[0].getName().equals("java.lang.Float")) {
                        setter.invoke(clazz, Float.valueOf(String.valueOf(value)));
                    }else if(parameterTypes[0].getName().equals("int")) {
                        setter.invoke(clazz, Integer.valueOf(String.valueOf(value)).intValue());
                    }else {
                        setter.invoke(clazz, (parameterTypes[0]).cast(value));
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

}
