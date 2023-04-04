package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.collection.CollUtil;
import com.suke.zhjg.common.autofull.cache.AutoFullRedisCache;
import com.suke.zhjg.common.autofull.sql.AutoFullSqlJdbcTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author czx
 * @title: AutoFullListHandler
 * @projectName zhjg-common-autofull
 * @description: 填充List
 * @date 2023/3/3115:21
 */
@Slf4j
public class AutoFullListHandler extends DefaultHandler {

    /**
     * 填充数据
     *
     * @param fullSql      处理后的填充sql
     * @param param        sql参数
     * @param useCache     是否使用缓存
     * @param sequence     当次调用方法key
     * @param field        当前字段
     * @param obj          当前bean
     * @param maxLevel     最大查询层级
     * @param currentLevel 当前层级
     * @param childLevel   是否支持查询子级
     **/
    public void full(String fullSql, Object param, boolean useCache, String sequence, Field field, Object obj, Object maxLevel, int currentLevel, boolean childLevel) {
        try {
            List<?> result;
            if (useCache) {
                // 取缓存
                List<?> data = AutoFullRedisCache.getList(sequence, fullSql, param, getBeanClassType(field));
                if (CollUtil.isNotEmpty(data)) {
                    result = data;
                } else {
                    Class<?> classType = getListClassType(field);
                    RowMapper<?> rm = BeanPropertyRowMapper.newInstance(classType);
                    result = AutoFullSqlJdbcTemplate.queryList(fullSql, rm, this.queryParamConvert(param));
                    AutoFullRedisCache.setData(sequence, fullSql, param, result);
                }
            } else {
                Class<?> classType = getListClassType(field);
                RowMapper<?> rm = BeanPropertyRowMapper.newInstance(classType);
                result = AutoFullSqlJdbcTemplate.queryList(fullSql, rm, this.queryParamConvert(param));
            }
            if (CollUtil.isNotEmpty(result)) {
                this.fullChildObj(result, sequence, maxLevel, currentLevel, childLevel, useCache);
                field.set(obj, result);
            }
        } catch (IllegalAccessException e) {
            log.error("填充List失败:{}", e);
            e.printStackTrace();
        }
    }
}
