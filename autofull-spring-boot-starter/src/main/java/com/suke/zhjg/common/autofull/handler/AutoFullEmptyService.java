package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullEmpty;
import com.suke.zhjg.common.autofull.constant.FullEmpty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author czx
 * @title: AutoFullJoinService
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2712:27
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullEmpty.class)
public class AutoFullEmptyService extends DefaultHandler {

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level) {
        try {
            if (annotation instanceof AutoFullEmpty) {
                AutoFullEmpty fieldAnnotation = field.getAnnotation(AutoFullEmpty.class);
                if (fieldAnnotation != null) {
                    field.setAccessible(true);
                    FullEmpty type = fieldAnnotation.type();
                    String value = type == FullEmpty.EMPTY ? null : "";
                    if(field.getType() == String.class){
                        field.set(obj, value);
                    }else {
                        field.set(obj, null);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("填充字段为null失败,{}", e);
            e.printStackTrace();
        }
    }
}
