package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.collection.CollUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullFieldSQL;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import com.suke.zhjg.common.autofull.sql.AutoFullSqlExecutor;
import com.suke.zhjg.common.autofull.util.StringSQLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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
        if(configProperties.isShowLog()){
            log.info("LEVEL:{}, SQL:{}",configProperties.getMaxLevel(),sql);
        }
        return sql;
    }

    @Override
    public void result(Annotation annotation,Field[] fields, Field field, Object obj,int level) {
        try {
            if(annotation instanceof AutoFullFieldSQL){
                AutoFullFieldSQL sqlAnnotation = field.getAnnotation(AutoFullFieldSQL.class);
                field.setAccessible(true);
                String alias = field.getName();
                String sql = sqlAnnotation.sql();
                Map<Integer,Object> param = getParam(fields,obj,sql);
                List<Map<String, Object>> result = AutoFullSqlExecutor.executeQuery(this.sql(sql,null), param);
                if(CollUtil.isNotEmpty(result)){
                    Object val = result.get(0).get(alias);
                    field.set(obj,val);
                }
            }
        }catch (IllegalAccessException e){
            log.error("填充字段失败:{}",e);
            e.printStackTrace();
        }
    }
}
