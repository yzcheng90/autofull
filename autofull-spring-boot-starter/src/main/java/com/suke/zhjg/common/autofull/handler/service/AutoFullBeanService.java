package com.suke.zhjg.common.autofull.handler.service;

import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullBean;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.handler.AutoFullBeanHandler;
import com.suke.zhjg.common.autofull.sequence.AutoSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author czx
 * @title: AutoFullFieldService
 * @projectName zhjg
 * @description: 填充处理bean 对象
 * @date 2020/8/2711:37
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullBean.class)
public class AutoFullBeanService extends AutoFullBeanHandler {

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level, boolean enableCache) {
        if (annotation instanceof AutoFullBean) {
            Object maxLevel = AutoSequence.init().get(sequence);
            AutoFullBean fieldAnnotation = field.getAnnotation(AutoFullBean.class);
            field.setAccessible(true);
            String table = fieldAnnotation.table();
            boolean useCache = fieldAnnotation.useCache();
            String tableField = fieldAnnotation.conditionField();
            boolean childLevel = fieldAnnotation.childLevel();
            Object param = findFieldValue(fields, tableField, obj);
            if (ObjectUtil.isNotNull(param)) {
                String sql = this.sql(table, tableField);
                if (configProperties.isShowLog()) {
                    log.info("ID:{}, LEVEL:{}, SQL:{}", sequence, level, sql);
                    log.info("ID:{}, LEVEL:{}, param：{}", sequence, level, param);
                }
                this.full(sql, param, enableCache && useCache, sequence, fields, field, obj, maxLevel, level, childLevel);
            }
        }
    }


}
