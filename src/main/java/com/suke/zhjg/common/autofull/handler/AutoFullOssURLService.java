package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.util.ObjectUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullOssUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @author czx
 * @title: AutoFullOssURLService
 * @projectName zhjg
 * @description: TODO
 * @date 2020/8/2712:27
 */
@Slf4j
@Component
@AutoFullConfiguration(type = AutoFullOssUrl.class)
public class AutoFullOssURLService implements Handler{

    @Value("${oss.preview-url}")
    public String previewUrl;

    @Value("${oss.bucket-name}")
    public String bucketName;

    @Override
    public String sql(String table, String queryField, String alias, String conditionField, String condition) {
        return null;
    }

    @Override
    public String sql(String sql, String conditionField) {
        return null;
    }

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj) {
        try {
            if(annotation instanceof AutoFullOssUrl){
                field.setAccessible(true);
                Object data = field.get(obj);
                if(ObjectUtil.isNotNull(data)){
                    String dataStr = (String) data;
                    if(dataStr.contains(previewUrl)){
                        field.set(obj,data);
                        return;
                    }
                }
                if(previewUrl != null && bucketName != null){
                    field.set(obj,previewUrl + "/" + bucketName + "/" + data);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("填充MinioURL失败,{}",e);
            e.printStackTrace();
        }
    }
}
