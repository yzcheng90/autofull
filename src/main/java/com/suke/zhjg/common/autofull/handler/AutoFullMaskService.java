package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullMask;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author czx
 * @title: AutoFullMaskService
 * @projectName zhjg-common-autofull
 * @description: TODO
 * @date 2020/11/2514:33
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullMask.class)
public class AutoFullMaskService implements Handler {

    @Autowired
    public ConfigProperties configProperties;

    private final String phoneStr = "****";
    private final String idCard = "********";

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        return null;
    }

    @Override
    public String sql(String sql, String conditionField) {
        return null;
    }

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj,int level) {
        try {
            if(annotation instanceof AutoFullMask){
                field.setAccessible(true);
                AutoFullMask autoFullMask = field.getAnnotation(AutoFullMask.class);
                String data = (String) field.get(obj);
                if(StrUtil.isNotEmpty(data) && StrUtil.isNotBlank(data)){
                    StringBuilder stringBuilder = new StringBuilder(data);
                    switch (autoFullMask.type()){
                        case phone:
                            if(data.length() == 11){
                                stringBuilder.replace(3,7,phoneStr);
                            }else if (data.length() > 3){
                                stringBuilder.replace(3,data.length() -1,idCard);
                            }
                            break;
                        case idCard:
                            if(data.length() == 18){
                                stringBuilder.replace(6,14,idCard);
                            }else if (data.length() > 6 ){
                                stringBuilder.replace(6,data.length() -1,idCard);
                            }
                            break;
                    }
                    data = stringBuilder.toString();
                    field.set(obj,data);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("填充数据脱敏失败,{}",e);
            e.printStackTrace();
        }
    }
}
