package com.suke.zhjg.common.autofull.decode;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import com.suke.zhjg.common.autofull.constant.Constant;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import com.suke.zhjg.common.autofull.util.CryptUtil;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @author czx
 * @title: DecodeMaskDataHandle
 * @projectName zhjg
 * @description: TODO
 * @date 2021/1/2814:16
 */
@Slf4j
@UtilityClass
public class DecodeMaskDataHandle {

    public String decode(String decrypt) {
        return getValue(decrypt);
    }

    @SneakyThrows
    public <T> T decode(T data) {
        Class<?> aClass = data.getClass();
        for (Field field : aClass.getDeclaredFields()) {
            Object value = field.get(data);
            field.setAccessible(true);
            String temp = String.valueOf(value);
            if (temp.contains(Constant.encryptStar) && temp.contains(getConfigProperties().getEncryptFlag())) {
                temp = DecodeMaskDataHandle.getValue(temp);
                field.set(data, temp);
            }
        }
        return data;
    }

    public <T> List<T> decode(List<T> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(obj -> obj = decode(obj));
        }
        return list;
    }

    public InputStream decode(InputStream in, ObjectMapper objectMapper, Type type) {
        String bodyStr = IoUtil.read(in, CharsetUtil.CHARSET_UTF_8);
        try {
            if (bodyStr.contains(Constant.encryptStar) && !bodyStr.contains(getConfigProperties().getEncryptFlag())) {
                throw new RuntimeException("脱敏数据解密失败，缺少key");
            }
            Object object = objectMapper.readValue(bodyStr, Class.forName(type.getTypeName()));
            Object decode = decode(object);
            bodyStr = objectMapper.writeValueAsString(decode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return IoUtil.toStream(bodyStr, CharsetUtil.CHARSET_UTF_8);
    }

    protected String getValue(String value) {
        if (StrUtil.isNotEmpty(value)) {
            if (value.contains(Constant.encryptStar) && value.contains(getConfigProperties().getEncryptFlag())) {
                int index = value.indexOf(getConfigProperties().getEncryptFlag());
                String key = value.substring(index + getConfigProperties().getEncryptFlag().length());
                if (StrUtil.isEmpty(key)) {
                    throw new RuntimeException("脱敏数据解密失败!key为空");
                }
                String decrypt = CryptUtil.decrypt(key);
                log.info("脱敏数据解密：{}", decrypt);
                return decrypt;
            } else {
                return value;
            }
        } else {
            throw new RuntimeException("脱敏字段密文为空!");
        }
    }

    private ConfigProperties getConfigProperties() {
        return ApplicationContextRegister.getApplicationContext().getBean(ConfigProperties.class);
    }

}
