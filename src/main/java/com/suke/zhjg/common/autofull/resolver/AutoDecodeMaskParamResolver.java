package com.suke.zhjg.common.autofull.resolver;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.suke.zhjg.common.autofull.annotation.AutoDecodeMask;
import com.suke.zhjg.common.autofull.constant.Constant;
import com.suke.zhjg.common.autofull.decode.DecodeMaskDataHandle;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author czx
 * @title: AutoDecodeMaskResolver
 * @projectName zhjg
 * @description: TODO param 参数拦截
 * @date 2021/1/2810:16
 */
@Slf4j
@Component
public class AutoDecodeMaskParamResolver implements HandlerMethodArgumentResolver {

    @Autowired
    public ConfigProperties configProperties;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.hasParameterAnnotation(AutoDecodeMask.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest webRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        Object obj = parameter.getParameterType().newInstance();
        webRequest.getParameterMap().forEach((key, value) -> {
            try {
                if(ArrayUtil.isNotEmpty(value)){
                    String temp = String.valueOf(value[0]);
                    if(temp.contains(Constant.phone) && !temp.contains(configProperties.getEncryptFlag())){
                        throw new RuntimeException("脱敏数据解密失败，缺少key");
                    }
                    if(temp.contains(Constant.phone) && temp.contains(configProperties.getEncryptFlag())){
                        log.info("key:{},value:{}",key,temp);
                        temp = DecodeMaskDataHandle.decode(temp);
                    }
                    BeanUtil.setProperty(obj, key, temp);
                }
            } catch (Exception e) {
                log.error("脱敏数据解密失败:", e);
            }
        });
        return obj;
    }
}
