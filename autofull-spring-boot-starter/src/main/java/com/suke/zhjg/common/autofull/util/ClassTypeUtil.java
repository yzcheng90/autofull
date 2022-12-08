package com.suke.zhjg.common.autofull.util;

import cn.hutool.core.date.DateUtil;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author czx
 * @title: ClassTypeUtil
 * @projectName zhjg
 * @description: TODO
 * @date 2020/12/314:21
 */
@UtilityClass
public class ClassTypeUtil {

    /**
     * 获取某一个字段上面的泛型参数数组,典型的就是获取List对象里面是啥参数
     *
     * @param f
     * @return
     */
    public Class<?>[] getParameterizedListType(Field f) {
        // 获取f字段的通用类型
        Type fc = f.getGenericType(); // 关键的地方得到其Generic的类型
        // 如果不为空并且是泛型参数的类型
        if (fc != null && fc instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) fc;
            Type[] types = pt.getActualTypeArguments();
            if (types != null && types.length > 0) {
                Class<?>[] classes = new Class<?>[types.length];
                for (int i = 0; i < classes.length; i++) {
                    classes[i] = (Class<?>) types[i];
                }
                return classes;
            }
        }
        Class<?>[] classes = new Class<?>[1];
        classes[0] = f.getType();
        return classes;
    }

    public void setValue(Object obj,Field field,Object value) throws IllegalAccessException {
        if (field.getType() == Integer.class) {
            if(value != null){
                int val = (Integer.valueOf(String.valueOf(value))).intValue();
                field.set(obj,val);
            }else {
                field.set(obj,0);
            }
        } else if (field.getType() == String.class) {
            String val = String.valueOf(value);
            field.set(obj,val);
        } else if (field.getType() == Double.class) {
            if(value != null){
                double val = (Double.valueOf(String.valueOf(value))).doubleValue();
                field.set(obj,val);
            }else {
                field.set(obj,0d);
            }
        } else if (field.getType() == Float.class) {
            if(value != null){
                float val = (Float.valueOf(String.valueOf(value))).floatValue();
                field.set(obj,val);
            }else {
                field.set(obj,0f);
            }
        } else if (field.getType() == Long.class) {
            if(value != null){
                long val = (Integer.valueOf(String.valueOf(value))).longValue();
                field.set(obj,val);
            }else {
                field.set(obj,0l);
            }
        } else if (field.getType() == Boolean.class) {
            if(value != null){
                boolean val = ((Boolean) value).booleanValue();
                field.set(obj,val);
            }else {
                field.set(obj,false);
            }
        } else if (field.getType() == Date.class) {
            if(value != null){
                if(value instanceof Date){
                    Date val = (Date) value;
                    field.set(obj,val);
                }else {
                    String dataStr = String.valueOf(value);
                    field.set(obj,DateUtil.parseDate(dataStr));
                }
            }else {
                field.set(obj,null);
            }
        }else if(field.getType() == int.class){
            if(value != null){
                int val = Integer.parseInt(String.valueOf(value));
                field.set(obj,val);
            }else {
                field.set(obj,0);
            }
        }
    }

}
