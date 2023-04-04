package com.suke.zhjg.common.autofull.handler.service;

import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullFieldSQL;
import com.suke.zhjg.common.autofull.handler.AutoFullFieldHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author czx
 * @title: AutoFullFieldService
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2711:37
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullFieldSQL.class)
public class AutoFullFieldSQLService extends AutoFullFieldHandler {

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level, boolean enableCache) {
        this.result(annotation, fields, field, obj, enableCache);
    }

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, boolean enableCache) {
        if (annotation instanceof AutoFullFieldSQL) {
            AutoFullFieldSQL sqlAnnotation = field.getAnnotation(AutoFullFieldSQL.class);
            field.setAccessible(true);
            String sql = sqlAnnotation.sql();
            boolean useCache = sqlAnnotation.useCache();
            List<Object> param = getParamList(fields, obj, sql);
            String parseSql = this.sql(sql);
            if (configProperties.isShowLog()) {
                log.info("SQL:{}", parseSql);
                log.info("param：{}", param);
            }
            this.full(parseSql, param, enableCache && useCache, field, obj);
        }
    }
}
