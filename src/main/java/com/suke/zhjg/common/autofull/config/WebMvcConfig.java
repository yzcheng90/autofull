package com.suke.zhjg.common.autofull.config;

import com.suke.zhjg.common.autofull.resolver.AutoDecodeMaskParamResolver;
import com.suke.zhjg.common.autofull.resolver.AutoDecodeMaskRequestBodyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author czx
 * @title: WebMvcConfig
 * @projectName zhjg
 * @description: TODO
 * @date 2021/1/2810:27
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(autoDecodeMaskResolver());
    }

    @Bean
    public AutoDecodeMaskParamResolver autoDecodeMaskResolver(){
        return new AutoDecodeMaskParamResolver();
    }

    @Bean
    public AutoDecodeMaskRequestBodyResolver autoDecodeMaskRequestBodyResolver(){
        return new AutoDecodeMaskRequestBodyResolver();
    }

}
