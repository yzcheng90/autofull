package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.cache.AutoFullRedisCache;
import com.suke.zhjg.common.autofull.sql.AutoFullSqlJdbcTemplate;
import com.suke.zhjg.common.autofull.util.ClassTypeUtil;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;

/**
 * @author czx
 * @title: AutoFullFieldHandler
 * @projectName zhjg-common-autofull
 * @description: 填充字段
 * @date 2023/3/3115:13
 */
@Slf4j
public class AutoFullFieldHandler extends DefaultHandler {

    /**
     * 填充数据
     *
     * @param fullSql  处理后的填充sql
     * @param param    sql参数
     * @param useCache 是否使用缓存
     * @param field    当前字段
     * @param obj      当前bean
     **/
    public void full(String fullSql, Object param, boolean useCache, Field field, Object obj) {
        try {
            Object result;
            Class<?> classType = getBeanClassType(field);
            if (useCache) {
                // 取缓存
                String stringData = AutoFullRedisCache.getStringData(null, fullSql, param);
                if (StrUtil.isNotEmpty(stringData)) {
                    result = stringData;
                } else {
                    result = AutoFullSqlJdbcTemplate.queryObj(fullSql, classType, this.queryParamConvert(param));
                    AutoFullRedisCache.setData(null, fullSql, param, result);
                }
            } else {
                result = AutoFullSqlJdbcTemplate.queryObj(fullSql, classType, this.queryParamConvert(param));
            }
            if (ObjectUtil.isNotEmpty(result)) {
                ClassTypeUtil.setValue(obj, field, result);
            }
        } catch (IllegalAccessException e) {
            log.error("填充字段失败:{}", e.getMessage());
            e.printStackTrace();
        }
    }

}
