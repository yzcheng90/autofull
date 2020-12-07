package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullBean;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import com.suke.zhjg.common.autofull.constant.ConstantSQL;
import com.suke.zhjg.common.autofull.sql.AutoFullSqlExecutor;
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
@AutoFullConfiguration(type = AutoFullBean.class)
public class AutoFullBeanService implements Handler {

    @Autowired
    public ConfigProperties configProperties;

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        String sql = ConstantSQL.SQL.SELECT + " * "+ ConstantSQL.SQL.FROM + " " + table + " " + ConstantSQL.SQL.WHERE + " " + conditionField + "  =  ?";
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
            if(annotation instanceof AutoFullBean){
                AutoFullBean fieldAnnotation = field.getAnnotation(AutoFullBean.class);
                field.setAccessible(true);
                String alias = field.getName();
                String table = fieldAnnotation.table();
                String tableField = fieldAnnotation.conditionField();
                Object param = findFieldValue(fields,tableField,obj);
                if(ObjectUtil.isNotNull(param)){
                    String sql = this.sql(table,null, alias, tableField,null);
                    Map<Integer,Object> paramMap = new HashMap<>();
                    paramMap.put(1,param);
                    List<?> result = AutoFullSqlExecutor.executeQuery(sql, getBeanClassType(field),paramMap,level);
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
