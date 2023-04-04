package com.suke.zhjg.common.autofull.handler.service;

import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullBeanSQL;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.handler.AutoFullBeanHandler;
import com.suke.zhjg.common.autofull.sequence.AutoSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author czx
 * @title: AutoFullFieldService
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2711:37
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullBeanSQL.class)
public class AutoFullBeanSQLService extends AutoFullBeanHandler {

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level, boolean enableCache) {
        if (annotation instanceof AutoFullBeanSQL) {
            Object maxLevel = AutoSequence.init().get(sequence);
            AutoFullBeanSQL fieldAnnotation = field.getAnnotation(AutoFullBeanSQL.class);
            field.setAccessible(true);
            String sql = fieldAnnotation.sql();
            boolean useCache = fieldAnnotation.useCache();
            boolean childLevel = fieldAnnotation.childLevel();
            Map<Integer, Object> param = getParam(fields, obj, sql);
            if (ObjectUtil.isNotNull(param)) {
                String fullSql = this.sql(sql);
                if (configProperties.isShowLog()) {
                    log.info("ID:{}, LEVEL:{}, SQL:{}", sequence, level, fullSql);
                    log.info("ID:{}, LEVEL:{}, param：{}", sequence, level, param);
                }
                List<Object> paramList = param.entrySet().stream().map(m -> m.getValue()).collect(Collectors.toList());
                this.full(fullSql, paramList, enableCache && useCache, sequence, fields, field, obj, maxLevel, level, childLevel);
            }
        }
    }

}
