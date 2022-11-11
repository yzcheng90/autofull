package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullOssUrl;
import com.suke.zhjg.common.autofull.entity.OssEntity;
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
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level) {
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
                                log.info("ID:{}, LEVEL:{}, 填充地址:{}", sequence, level, url);
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
