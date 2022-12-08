package com.suke.zhjg.common.autofull.handler;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author czx
 * @title: Handler
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2711:31
 */
public interface Handler {

    String sql(String sql);

    String sql(String sql, String conditionField);

    String sql(String table, String alias, String conditionField);

    String sql(String table, String queryField, String alias, String conditionField);

    String sql(String table, String queryField, String alias, String conditionField, String condition);

    void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level);


}
