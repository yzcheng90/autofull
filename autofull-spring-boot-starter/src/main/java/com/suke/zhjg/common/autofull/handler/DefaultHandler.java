package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import com.suke.zhjg.common.autofull.constant.ConstantSQL;
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
        Matcher matcher = StringSQLUtil.parse(sql);
        while (matcher.find()) {
            String fieldKey = matcher.group(1);
            sql = sql.replace("{" + fieldKey + "}", " ? ");
        }
        return sql;
    }

    @Override
    public String sql(String table, String conditionField) {
        conditionField = getConditionField(conditionField);
        String sql = ConstantSQL.SQL.SELECT + " * " + ConstantSQL.SQL.FROM + " " + table + " " + ConstantSQL.SQL.WHERE + " " + conditionField + "  =  ? ";
        return sql;
    }

    @Override
    public String sql(String table, String alias, String conditionField) {
        if (StrUtil.isEmpty(alias)) {
            return this.sql(table, conditionField);
        }
        conditionField = getConditionField(conditionField);
        String sql = ConstantSQL.SQL.SELECT + " " + alias + " " + ConstantSQL.SQL.FROM + " " + table + " " + ConstantSQL.SQL.WHERE + " " + conditionField + "  =  ? ";
        return sql;
    }

    @Override
    public String sql(String table, String queryField, String alias, String conditionField) {
        alias = getConditionField(alias);
        queryField = getConditionField(queryField);
        String field = StrUtil.isBlank(queryField) ? alias : queryField + " " + ConstantSQL.SQL.AS + " " + alias;
        conditionField = getConditionField(conditionField);
        String sql = ConstantSQL.SQL.SELECT + " " + field + " " + ConstantSQL.SQL.FROM + " " + table + " " + ConstantSQL.SQL.WHERE + " " + conditionField + "  =  ?";
        return sql;
    }

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        return null;
    }

    @Override
    public void result(Annotation annotation, Field field, Object obj) {

    }

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj) {

    }

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, boolean enableCache) {

    }

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level, boolean enableCache) {
        if (fields == null) {
            this.result(annotation, field, obj);
        }
    }

    /**
     * 填充层级（子级）
     *
     * @param result       数据对象
     * @param sequence     当前请求的key
     * @param maxLevel     最大填充层级
     * @param currentLevel 当前层级
     * @param childLevel   是否支持填充子级
     * @param enableCache  是否使用缓存
     **/
    public void fullChildObj(List<?> result, String sequence, Object maxLevel, int currentLevel, boolean childLevel, boolean enableCache) {
        if (CollUtil.isNotEmpty(result)) {
            if (ObjectUtil.isNotNull(maxLevel)) {
                int max = (int) maxLevel;
                if (currentLevel < max && childLevel) {
                    currentLevel += 1;
                    AutoFullHandler.full(result, sequence, currentLevel, enableCache);
                }
            }
        }
    }

    /**
     * 获取条件字段的值
     **/
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

    /**
     * 参数转换
     **/
    public Object[] queryParamConvert(Object param) {
        Object[] paramObj;
        if (param instanceof ArrayList) {
            paramObj = ((ArrayList) param).toArray();
        } else {
            paramObj = new Object[]{param};
        }
        return paramObj;
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

    /**
     * 获取当前条件字段的数据库字段
     **/
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
