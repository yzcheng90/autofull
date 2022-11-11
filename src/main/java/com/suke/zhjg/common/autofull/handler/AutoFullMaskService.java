package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullMask;
import com.suke.zhjg.common.autofull.constant.Constant;
import com.suke.zhjg.common.autofull.util.CryptUtil;
import lombok.extern.slf4j.Slf4j;
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
public class AutoFullMaskService extends DefaultHandler {

    @Override
    public void result(Annotation annotation, Field[] fields, Field field, Object obj, String sequence, int level) {
        try {
            if (annotation instanceof AutoFullMask) {
                field.setAccessible(true);
                AutoFullMask autoFullMask = field.getAnnotation(AutoFullMask.class);
                String data = (String) field.get(obj);
                if (StrUtil.isNotEmpty(data) && StrUtil.isNotBlank(data)) {
                    StringBuilder stringBuilder = new StringBuilder(data);
                    String cutValue = "";
                    switch (autoFullMask.type()) {
                        case phone:
                            if (data.length() == 11) {
                                cutValue = stringBuilder.substring(3, 7);
                                stringBuilder.replace(3, 7, Constant.phone);
                            } else if (data.length() > 3) {
                                cutValue = stringBuilder.substring(3, data.length() - 1);
                                stringBuilder.replace(3, data.length() - 1, Constant.idCard);
                            }
                            break;
                        case idCard:
                            if (data.length() == 18) {
                                cutValue = stringBuilder.substring(6, 14);
                                stringBuilder.replace(6, 14, Constant.idCard);
                            } else if (data.length() > 6) {
                                cutValue = stringBuilder.substring(6, data.length() - 1);
                                stringBuilder.replace(6, data.length() - 1, Constant.idCard);
                            }
                            break;
                    }
                    data = stringBuilder.toString();
                    String encrypt = CryptUtil.encrypt(cutValue);
                    if (configProperties.isShowLog()) {
                        log.info("ID:{}, LEVEL:{}, 脱敏数据加密：{} 密钥：{}", sequence, level, cutValue, encrypt);
                    }

                    field.set(obj, data + configProperties.getEncryptFlag() + encrypt);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("填充数据脱敏失败,{}", e);
            e.printStackTrace();
        }
    }
}
