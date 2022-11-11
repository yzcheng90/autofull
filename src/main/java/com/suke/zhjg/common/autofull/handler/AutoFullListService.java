package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullList;
import com.suke.zhjg.common.autofull.cache.AutoFullRedisCache;
import com.suke.zhjg.common.autofull.constant.ConstantSQL;
import com.suke.zhjg.common.autofull.sequence.AutoSequence;
import com.suke.zhjg.common.autofull.sql.AutoFullSqlJdbcTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
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
public class AutoFullListService extends DefaultHandler {

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        String sql = ConstantSQL.SQL.SELECT + " * " + ConstantSQL.SQL.FROM + " " + table + " " + ConstantSQL.SQL.WHERE + " " + conditionField + "  =  ? ";
        return sql;
    }

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level) {
        try {
            if (annotation instanceof AutoFullList) {
                Object object = AutoSequence.init().get(sequence);
                AutoFullList fieldAnnotation = field.getAnnotation(AutoFullList.class);
                field.setAccessible(true);
                String alias = field.getName();
                String table = fieldAnnotation.table();
                boolean useCache = fieldAnnotation.useCache();
                String tableField = fieldAnnotation.conditionField();
                Object param = findFieldValue(fields, tableField, obj);
                if (ObjectUtil.isNotNull(param)) {
                    String parseSql = this.sql(table, null, alias, tableField, null) + " " + fieldAnnotation.orderBy();
                    Map<Integer, Object> paramMap = new HashMap<>();
                    paramMap.put(1, param);
                    if (configProperties.isShowLog()) {
                        log.info("ID:{}, LEVEL:{}, SQL:{}", sequence, level, parseSql);
                        log.info("ID:{}, LEVEL:{}, param：{}", sequence, level, param);
                    }

                    List<?> result = null;
                    if (useCache) {
                        // 取缓存
                        List<?> data = AutoFullRedisCache.getList(sequence, parseSql, param, getBeanClassType(field));
                        if (CollUtil.isNotEmpty(data)) {
                            result = data;
                        } else {
                            Class<?> classType = getListClassType(field);
                            RowMapper<?> rm = BeanPropertyRowMapper.newInstance(classType);
                            result = AutoFullSqlJdbcTemplate.queryList(parseSql, rm, param);
                            AutoFullRedisCache.setData(sequence, parseSql, param, result);
                        }
                    } else {
                        Class<?> classType = getListClassType(field);
                        RowMapper<?> rm = BeanPropertyRowMapper.newInstance(classType);
                        result = AutoFullSqlJdbcTemplate.queryList(parseSql, rm, param);
                    }

                    if (CollUtil.isNotEmpty(result)) {
                        if (ObjectUtil.isNotNull(object)) {
                            int maxLevel = (int) object;
                            if (level < maxLevel && fieldAnnotation.childLevel()) {
                                level += 1;
                                AutoFullHandler.full(result, sequence, level);
                            }
                        }
                        field.set(obj, result);
                    }
                }
            }

        } catch (IllegalAccessException e) {
            log.error("填充List失败:{}", e);
            e.printStackTrace();
        }
    }
}
