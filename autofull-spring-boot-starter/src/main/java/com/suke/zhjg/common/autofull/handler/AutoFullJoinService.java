package com.suke.zhjg.common.autofull.handler;

import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullJoin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author czx
 * @title: AutoFullJoinService
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2712:27
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullJoin.class)
public class AutoFullJoinService extends DefaultHandler {

    public final String pattern = "(?!\\{)([^\\{\\}]+)(?=\\})";

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level) {
        try {
            if (annotation instanceof AutoFullJoin) {
                AutoFullJoin fieldAnnotation = field.getAnnotation(AutoFullJoin.class);
                if (fieldAnnotation != null) {
                    field.setAccessible(true);
                    String value = fieldAnnotation.value();
                    Matcher matcher = Pattern.compile(pattern).matcher(value);
                    while (matcher.find()) {
                        String key = matcher.group(1);
                        Object param = findFieldValue(fields, key, obj);
                        value = value.replace("{" + key + "}", param != null ? param.toString() : "");
                    }
                    field.set(obj, value);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("填充字符连接失败,{}", e);
            e.printStackTrace();
        }
    }
}
