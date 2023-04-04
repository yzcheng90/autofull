package com.suke.zhjg.common.autofull.handler.service;

import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullListSQL;
import com.suke.zhjg.common.autofull.handler.AutoFullListHandler;
import com.suke.zhjg.common.autofull.sequence.AutoSequence;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * @author czx
 * @title: AutoFullListSQLService
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2711:37
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullListSQL.class)
public class AutoFullListSQLService extends AutoFullListHandler {

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level, boolean enableCache) {
        if (annotation instanceof AutoFullListSQL) {
            Object maxLevel = AutoSequence.init().get(sequence);
            AutoFullListSQL fieldAnnotation = field.getAnnotation(AutoFullListSQL.class);
            field.setAccessible(true);
            String sql = fieldAnnotation.sql();
            boolean useCache = fieldAnnotation.useCache();
            boolean childLevel = fieldAnnotation.childLevel();
            Map<Integer, Object> param = getParam(fields, obj, sql);
            if (ObjectUtil.isNotNull(param)) {
                String parseSql = this.sql(sql);
                if (configProperties.isShowLog()) {
                    log.info("ID:{}, LEVEL:{}, SQL:{}", sequence, level, parseSql);
                    log.info("ID:{}, LEVEL:{}, param：{}", sequence, level, param);
                }
                List<Object> paramList = getParamList(fields, obj, sql);
                this.full(parseSql, paramList, enableCache && useCache, sequence, field, obj, maxLevel, level, childLevel);
            }
        }
    }
}
