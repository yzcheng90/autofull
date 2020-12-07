package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullBeanSQL;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
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
@AutoFullConfiguration(type = AutoFullBeanSQL.class)
public class AutoFullBeanSQLService implements Handler {

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
    public void result(Annotation annotation , Field[] fields, Field field, Object obj,int level) {
        try {
            if(annotation instanceof AutoFullBeanSQL){
                AutoFullBeanSQL fieldAnnotation = field.getAnnotation(AutoFullBeanSQL.class);
                field.setAccessible(true);
                String sql = fieldAnnotation.sql();
                Map<Integer,Object> param = getParam(fields,obj,sql);
                if(ObjectUtil.isNotNull(param)){
                    List<?> result = AutoFullSqlExecutor.executeQuery(this.sql(sql,null), getBeanClassType(field),param,level);
                    if(CollUtil.isNotEmpty(result)){
                        if(level < configProperties.getMaxLevel() && fieldAnnotation.childLevel()){
                            level += 1;
                            AutoFullHandler.full(result,level);
                        }
                        field.set(obj,result.get(0));
                    }
                }
            }

        }catch (IllegalAccessException e){
            log.error("填充Bean失败:{}",e);
            e.printStackTrace();
        }
    }


}
