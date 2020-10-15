package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullList;
import com.suke.zhjg.common.autofull.constant.ConstantSQL;
import com.suke.zhjg.common.autofull.sql.AutoFullSqlExecutor;
import lombok.extern.slf4j.Slf4j;
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
public class AutoFullListService implements Handler{

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        String sql = ConstantSQL.SQL.SELECT + " * "+ ConstantSQL.SQL.FROM + " " + table + " " + ConstantSQL.SQL.WHERE + " " + conditionField + "  =  ?";
        log.info("SQL:{}",sql);
        return sql;
    }

    @Override
    public String sql(String sql, String conditionField) {
        return null;
    }

    @Override
    public void result(Annotation annotation , Field[] fields, Field field, Object obj) {
        try {
            if(annotation instanceof AutoFullList){
                AutoFullList fieldAnnotation = field.getAnnotation(AutoFullList.class);
                field.setAccessible(true);
                String alias = field.getName();
                String table = fieldAnnotation.table();
                String tableField = fieldAnnotation.conditionField();
                Object param = findFieldValue(fields,tableField,obj);
                if(ObjectUtil.isNotNull(param)){
                    String parseSql = this.sql(table,null, alias, tableField,null);
                    Map<Integer,Object> paramMap = new HashMap<>();
                    paramMap.put(1,param);
                    List<Map<String, Object>> result = AutoFullSqlExecutor.executeQuery(parseSql, paramMap);
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
