package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullListSQL;
import com.suke.zhjg.common.autofull.sql.AutoFullSqlExecutor;
import com.suke.zhjg.common.autofull.util.StringSQLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

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
public class AutoFullListSQLService implements Handler{

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
        log.info("SQL:{}",sql);
        return sql;
    }

    @Override
    public void result(Annotation annotation , Field[] fields, Field field, Object obj) {
        try {
            if(annotation instanceof AutoFullListSQL){
                AutoFullListSQL fieldAnnotation = field.getAnnotation(AutoFullListSQL.class);
                field.setAccessible(true);
                String sql = fieldAnnotation.sql();
                Map<Integer,Object> param = getParam(fields,obj,sql);
                if(ObjectUtil.isNotNull(param)){
                    List<Map<String, Object>> result = AutoFullSqlExecutor.executeQuery(this.sql(sql,null), param);
                    if(CollUtil.isNotEmpty(result)){
                        field.set(obj,result);
                    }
                }
            }

        }catch (IllegalAccessException e){
            log.error("填充List失败:{}",e);
            e.printStackTrace();
        }
    }
}
