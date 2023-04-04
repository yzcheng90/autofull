package com.suke.zhjg.common.autofull.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author czx
 * @title: Handler
 * @projectName zhjg
 * @description: 处理填充的接口
 * @date 2020/8/2711:31
 */
public interface Handler {

    /**
     * 查询的SQL
     *
     * @param sql sql
     **/
    String sql(String sql);

    /**
     * 查询的SQL
     *
     * @param table          查询的表名
     * @param conditionField 查询条件的字段
     **/
    String sql(String table, String conditionField);

    /**
     * 查询的SQL
     *
     * @param table          查询的表名
     * @param alias          查询返回的别名
     * @param conditionField 查询条件的字段
     **/
    String sql(String table, String alias, String conditionField);

    /**
     * 查询的SQL
     *
     * @param table          查询的表名
     * @param queryField     查询的字段名
     * @param alias          查询返回的别名
     * @param conditionField 查询条件的字段
     **/
    String sql(String table, String queryField, String alias, String conditionField);

    /**
     * 查询的SQL
     *
     * @param table          查询的表名
     * @param queryField     查询的字段名
     * @param alias          查询返回的别名
     * @param conditionField 查询条件的字段
     * @param condition      自定义条件
     **/
    String sql(String table, String queryField, String alias, String conditionField, String condition);

    /**
     * 处理填充数据
     *
     * @param annotation 注解对象
     * @param field      当前字段
     * @param obj        当前对象，设置填充值用
     **/
    void result(Annotation annotation, Field field, Object obj);

    /**
     * 处理填充数据
     *
     * @param annotation 注解对象
     * @param fields     当前对象字段
     * @param field      当前字段
     * @param obj        当前对象，设置填充值用
     **/
    void result(Annotation annotation, Field[] fields, Field field, Object obj);

    /**
     * 处理填充数据
     *
     * @param annotation  注解对象
     * @param fields      当前对象字段
     * @param field       当前字段
     * @param obj         当前对象，设置填充值用
     * @param enableCache 当次调用是否使用缓存
     **/
    void result(Annotation annotation, Field[] fields, Field field, Object obj, boolean enableCache);

    /**
     * 处理填充数据
     *
     * @param annotation  注解对象
     * @param fields      当前对象字段
     * @param field       当前字段
     * @param obj         当前对象，设置填充值用
     * @param sequence    当前调用方法的序列号
     * @param level       调用层级
     * @param enableCache 当次调用是否使用缓存
     **/
    void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level, boolean enableCache);


}
