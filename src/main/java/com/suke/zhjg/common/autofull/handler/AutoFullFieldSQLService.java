package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullFieldSQL;
import com.suke.zhjg.common.autofull.cache.AutoFullRedisCache;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import com.suke.zhjg.common.autofull.sequence.AutoSequence;
import com.suke.zhjg.common.autofull.sql.AutoFullSqlJdbcTemplate;
import com.suke.zhjg.common.autofull.util.ClassTypeUtil;
import com.suke.zhjg.common.autofull.util.StringSQLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.regex.Matcher;

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
public class AutoFullFieldSQLService implements Handler {

    @Autowired
    public ConfigProperties configProperties;

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        return null;
    }

    @Override
    public String sql(String sql, String conditionField) {
        Matcher matcher = StringSQLUtil.parse(sql);
        while(matcher.find()){
            String fieldKey = matcher.group(1);
            sql = sql.replace("{" + fieldKey + "}"," ? ");
        }
        return sql;
    }

    @Override
    public void result(Annotation annotation,Field[] fields, Field field, Object obj,String sequence,int level) {
        try {
            if(annotation instanceof AutoFullFieldSQL){
                Object object = AutoSequence.init().get(sequence);
                AutoFullFieldSQL sqlAnnotation = field.getAnnotation(AutoFullFieldSQL.class);
                field.setAccessible(true);
                String alias = field.getName();
                String sql = sqlAnnotation.sql();
                boolean useCache = sqlAnnotation.useCache();
                List<Object> param = getParamList(fields,obj,sql);
                String parseSql = this.sql(sql, null);
                if(configProperties.isShowLog()){
                    log.info("ID:{}, LEVEL:{}, SQL:{}",sequence,level,parseSql);
                    log.info("ID:{}, LEVEL:{}, param：{}",sequence,level,param);
                }
                Object[] paramArray = param.toArray();
                String result = null;
                if(useCache){
                    // 取缓存
                    String stringData = AutoFullRedisCache.getStringData(parseSql, param);
                    if(StrUtil.isNotEmpty(stringData)){
                        result = stringData;
                    }else {
                        result = AutoFullSqlJdbcTemplate.queryObj(parseSql, String.class, paramArray);
                        AutoFullRedisCache.setData(parseSql,param,result);
                    }
                }else {
                    result = AutoFullSqlJdbcTemplate.queryObj(parseSql, String.class, paramArray);
                }
                if(StrUtil.isNotEmpty(result)){
                    ClassTypeUtil.setValue(obj,field,result);
                }
            }
        }catch (IllegalAccessException e){
            log.error("填充字段失败:{}",e);
            e.printStackTrace();
        }
    }
}
