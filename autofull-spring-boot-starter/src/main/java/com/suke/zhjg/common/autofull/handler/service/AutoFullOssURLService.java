package com.suke.zhjg.common.autofull.handler.service;

import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullOssUrl;
import com.suke.zhjg.common.autofull.entity.OssEntity;
import com.suke.zhjg.common.autofull.handler.DefaultHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AutoFullOssURLService extends DefaultHandler {

    @Autowired
    public OssEntity ossEntity;

    @Override
    public void result(Annotation annotation, Field field, Object obj) {
        try {
            if (annotation instanceof AutoFullOssUrl) {
                field.setAccessible(true);
                String data = (String) field.get(obj);
                if (StrUtil.isNotEmpty(data) && StrUtil.isNotBlank(data)) {
                    String previewUrl = ossEntity.getPreviewUrl();
                    String bucketName = ossEntity.getBucketName();
                    if (data.contains(previewUrl)) {
                        field.set(obj, data);
                        return;
                    } else {
                        if (previewUrl != null && bucketName != null) {
                            String url = previewUrl + "/" + bucketName + "/" + data;
                            if (configProperties.isShowLog()) {
                                log.info("填充地址:{}", url);
                            }
                            field.set(obj, url);
                        } else if (previewUrl != null) {
                            String url = previewUrl + "/" + data;
                            if (configProperties.isShowLog()) {
                                log.info("填充地址:{}", url);
                            }
                            field.set(obj, url);
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            log.error("填充MinioURL失败,{}", e);
            e.printStackTrace();
        }
    }
}
