package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;
import com.suke.zhjg.common.autofull.annotation.AutoFullConfiguration;
import com.suke.zhjg.common.autofull.annotation.AutoFullMask;
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
                    String cutValue = "";
                    switch (autoFullMask.type()) {
                        case CHINESE_NAME:
                            cutValue = DesensitizedUtil.chineseName(data);
                            break;
                        case ID_CARD:
                            cutValue = DesensitizedUtil.idCardNum(data,1,2);
                            break;
                        case FIXED_PHONE:
                            cutValue = DesensitizedUtil.fixedPhone(data);
                            break;
                        case MOBILE_PHONE:
                            cutValue = DesensitizedUtil.mobilePhone(data);
                            break;
                        case ADDRESS:
                            cutValue = DesensitizedUtil.address(data,8);
                            break;
                        case EMAIL:
                            cutValue = DesensitizedUtil.email(data);
                            break;
                        case PASSWORD:
                            cutValue = DesensitizedUtil.password(data);
                            break;
                        case CAR_LICENSE:
                            cutValue = DesensitizedUtil.carLicense(data);
                            break;
                        case BANK_CARD:
                            cutValue = DesensitizedUtil.bankCard(data);
                            break;
                    }
                    String encrypt = CryptUtil.encrypt(data);
                    if (configProperties.isShowLog()) {
                        log.info("ID:{}, LEVEL:{}, 脱敏数据加密：{} 密钥：{}", sequence, level, cutValue, encrypt);
                    }
                    field.set(obj, cutValue + configProperties.getEncryptFlag() + encrypt);
                }
            }
        } catch (IllegalAccessException e) {
            log.error("填充数据脱敏失败,{}", e);
            e.printStackTrace();
        }
    }
}
