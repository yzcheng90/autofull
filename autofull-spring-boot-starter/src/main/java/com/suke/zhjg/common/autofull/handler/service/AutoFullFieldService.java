package com.suke.zhjg.common.autofull.handler.service;

import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullField;
import com.suke.zhjg.common.autofull.handler.AutoFullFieldHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author czx
 * @title: AutoFullFieldService
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2711:37
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullField.class)
public class AutoFullFieldService extends AutoFullFieldHandler {

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level, boolean enableCache) {
        this.result(annotation, fields, field, obj, enableCache);
    }

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, boolean enableCache) {
        if (annotation instanceof AutoFullField) {
            AutoFullField fieldAnnotation = field.getAnnotation(AutoFullField.class);
            field.setAccessible(true);
            String alias = field.getName();
            String table = fieldAnnotation.table();
            String tableField = fieldAnnotation.conditionField();
            String queryField = fieldAnnotation.queryField();
            boolean useCache = fieldAnnotation.useCache();
            Object param = findFieldValue(fields, tableField, obj);
            if (ObjectUtil.isNotNull(param)) {
                String parseSql = this.sql(table, queryField, alias, tableField);
                if (configProperties.isShowLog()) {
                    log.info("SQL:{}", parseSql);
                    log.info("param：{}", param);
                }
                this.full(parseSql, param, enableCache && useCache, field, obj);
            }
        }
    }
}
