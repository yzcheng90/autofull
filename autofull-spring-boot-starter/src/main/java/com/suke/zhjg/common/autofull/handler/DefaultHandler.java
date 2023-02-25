package com.suke.zhjg.common.autofull.handler;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import com.suke.zhjg.common.autofull.util.ClassTypeUtil;
import com.suke.zhjg.common.autofull.util.FieldCaseUtil;
import com.suke.zhjg.common.autofull.util.StringSQLUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author czx
 * @title: publicHandler
 * @projectName zhjg-common-autofull
 * @description: TODO 默认空实现
 * @date 2022/11/813:34
 */
public abstract class DefaultHandler implements Handler {

    @Autowired
    public ConfigProperties configProperties;

    @Override
    public String sql(String sql) {
        return null;
    }

    @Override
    public String sql(String sql, String conditionField) {
        return null;
    }

    @Override
    public String sql(String table, String alias, String conditionField) {
        return null;
    }

    @Override
    public String sql(String table, String queryField, String alias, String conditionField) {
        return null;
    }

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        return null;
    }


    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level) {

    }

    public Object findFieldValue(Field[] fields, String key, Object obj) {
        for (Field field : fields) {
            if (field.getName().equals(key)) {
                try {
                    if (field.getType().getName().toLowerCase().equals("int")) {
                        return String.valueOf((int) field.get(obj));
                    } else if (field.getType().getName().toLowerCase().equals("long")) {
                        return String.valueOf((long) field.get(obj));
                    } else if (field.getType().getName().toLowerCase().equals("integer")) {
                        return String.valueOf((long) field.get(obj));
                    } else {
                        return field.get(obj);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public Map<Integer, Object> getParam(Field[] fields, Object obj, String sql) {
        Map<Integer, Object> paramMap = new HashMap<>();
        Matcher matcher = StringSQLUtil.parse(sql);
        int index = 1;
        while (matcher.find()) {
            String fieldKey = matcher.group(1);
            Object param = findFieldValue(fields, fieldKey, obj);
            paramMap.put(index, param);
            index++;
        }
        return paramMap;
    }

    public List<Object> getParamList(Field[] fields, Object obj, String sql) {
        List<Object> paramList = new ArrayList<>();
        Matcher matcher = StringSQLUtil.parse(sql);
        while (matcher.find()) {
            String fieldKey = matcher.group(1);
            Object param = findFieldValue(fields, fieldKey, obj);
            paramList.add(param);
        }
        return paramList;
    }

    public Class<?> getListClassType(Field field) {
        Class<?>[] parameterizedType = ClassTypeUtil.getParameterizedListType(field);
        return parameterizedType[0];
    }

    public Class<?> getBeanClassType(Field field) {
        Class<?> parameterizedType = field.getType();
        return parameterizedType;
    }

    public Class<?> getClassType(Field field) {
        Class<?> parameterizedType = field.getType();
        return parameterizedType;
    }

    public String getConditionField(String field) {
        boolean fieldRule = configProperties.isFieldRule();
        if (fieldRule) {
            MybatisPlusProperties bean = ApplicationContextRegister.getApplicationContext().getBean(MybatisPlusProperties.class);
            MybatisConfiguration configuration = bean.getConfiguration();
            if (configuration == null || configuration.isMapUnderscoreToCamelCase()) {
                if (field.contains("_")) {
                    return FieldCaseUtil.toCamelCase(field);
                } else {
                    return FieldCaseUtil.toUnderScoreCase(field);
                }
            } else {
                return FieldCaseUtil.toUnderScoreCase(field);
            }
        } else {
            return field;
        }
    }

}
