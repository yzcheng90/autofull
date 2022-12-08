package com.suke.zhjg.common.autofull.config;

import cn.hutool.core.collection.CollUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * @author czx
 * @title: AutoConfig
 * @projectName zhjg
 * @description: TODO
 * @date 2020/9/711:21
 */
@Slf4j
@Component
public class AutoConfig implements InitializingBean {

    @Getter
    private Map<String, Object> beansWithAnnotation = new HashMap<>();

    @Override
    public void afterPropertiesSet(){
        this.beansWithAnnotation = ApplicationContextRegister.getApplicationContext().getBeansWithAnnotation(AutoFullConfiguration.class);
    }

    public Object findBean(Annotation annotation){
        if(CollUtil.isNotEmpty(beansWithAnnotation)){
            for (String key : beansWithAnnotation.keySet()) {
                Class<?> bean = beansWithAnnotation.get(key).getClass();
                final AutoFullConfiguration customAnnotation = AnnotationUtils.findAnnotation(beansWithAnnotation.get(key).getClass(), AutoFullConfiguration.class);
                if (null != customAnnotation && customAnnotation.type() != null && customAnnotation.type() == annotation.annotationType()) {
                    return ApplicationContextRegister.getApplicationContext().getBean(bean);
                }
            }
        }
        return null;
    }
}
