package com.suke.zhjg.common.autofull.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suke.zhjg.common.autofull.annotation.AutoDecodeMask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author czx
 * @title: AutoDecodeMaskRequestBodyResolver
 * @projectName zhjg
 * @description: TODO requestBody参数拦截
 * @date 2021/1/2813:46
 */
@ControllerAdvice
public class AutoDecodeMaskRequestBodyResolver implements  RequestBodyAdvice  {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return methodParameter.hasParameterAnnotation(AutoDecodeMask.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) throws IOException {
        try {
            return new CustomHttpInputMessage(httpInputMessage,objectMapper,type);
        }catch (Exception e){
            throw new RuntimeException("脱敏数据解密失败：" + methodParameter.getMethod().getName());
        }
    }

    @Override
    public Object afterBodyRead(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }

    @Override
    public Object handleEmptyBody(Object o, HttpInputMessage httpInputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> aClass) {
        return o;
    }
}
