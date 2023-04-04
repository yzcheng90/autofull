package com.suke.zhjg.common.autofull.handler.service;

import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullList;
import com.suke.zhjg.common.autofull.handler.AutoFullListHandler;
import com.suke.zhjg.common.autofull.sequence.AutoSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author czx
 * @title: AutoFullFieldService
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2711:37
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullList.class)
public class AutoFullListService extends AutoFullListHandler {

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level, boolean enableCache) {
        if (annotation instanceof AutoFullList) {
            Object maxLevel = AutoSequence.init().get(sequence);
            AutoFullList fieldAnnotation = field.getAnnotation(AutoFullList.class);
            field.setAccessible(true);
            String table = fieldAnnotation.table();
            boolean childLevel = fieldAnnotation.childLevel();
            boolean useCache = fieldAnnotation.useCache();
            String tableField = fieldAnnotation.conditionField();
            Object param = findFieldValue(fields, tableField, obj);
            if (ObjectUtil.isNotNull(param)) {
                String parseSql = this.sql(table, tableField) + " " + fieldAnnotation.orderBy();
                Map<Integer, Object> paramMap = new HashMap<>();
                paramMap.put(1, param);
                if (configProperties.isShowLog()) {
                    log.info("ID:{}, LEVEL:{}, SQL:{}", sequence, level, parseSql);
                    log.info("ID:{}, LEVEL:{}, param：{}", sequence, level, param);
                }
                this.full(parseSql, param, enableCache && useCache, sequence, field, obj, maxLevel, level, childLevel);
            }
        }
    }
}
