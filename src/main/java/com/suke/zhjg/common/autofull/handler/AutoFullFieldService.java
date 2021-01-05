package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullField;
import com.suke.zhjg.common.autofull.constant.ConstantSQL;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import com.suke.zhjg.common.autofull.sql.AutoFullSqlExecutor;
import com.suke.zhjg.common.autofull.util.ClassTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@AutoFullConfiguration(type = AutoFullField.class)
public class AutoFullFieldService implements Handler {

    @Autowired
    public ConfigProperties configProperties;

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        String field = StrUtil.isBlank(queryField) ? alias : queryField + " " + ConstantSQL.SQL.AS + " " + alias;
        String sql = ConstantSQL.SQL.SELECT + " " + field + " "+ ConstantSQL.SQL.FROM + " " + table + " " + ConstantSQL.SQL.WHERE + " " + conditionField + "  =  ?";
        if(configProperties.isShowLog()){
            log.info("LEVEL:{}, SQL:{}",configProperties.getCurrLevel(),sql);
        }
        return sql;
    }

    @Override
    public String sql(String sql, String conditionField) {
        return null;
    }

    @Override
    public void result(Annotation annotation , Field[] fields, Field field, Object obj,int level) {
        try {
            if(annotation instanceof AutoFullField){
                AutoFullField fieldAnnotation = field.getAnnotation(AutoFullField.class);
                field.setAccessible(true);
                String alias = field.getName();
                String table = fieldAnnotation.table();
                String tableField = fieldAnnotation.conditionField();
                String queryField = fieldAnnotation.queryField();
                Object param = findFieldValue(fields,tableField,obj);
                if(ObjectUtil.isNotNull(param)){
                    String parseSql = this.sql(table,queryField, alias, tableField,null);
                    Map<Integer,Object> paramMap = new HashMap<>();
                    paramMap.put(1,param);
                    List<Map<String, Object>> result = AutoFullSqlExecutor.executeQuery(parseSql, paramMap);
                    if(CollUtil.isNotEmpty(result)){
                        Object val = result.get(0).get(alias);
                        ClassTypeUtil.setValue(obj,field,val);
                    }
                }
            }

        }catch (IllegalAccessException e){
            log.error("填充字段失败:{}",e);
            e.printStackTrace();
        }
    }
}
