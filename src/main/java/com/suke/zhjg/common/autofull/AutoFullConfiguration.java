package com.suke.zhjg.common.autofull;

import com.suke.zhjg.common.autofull.aop.AutoFullAspect;
import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import com.suke.zhjg.common.autofull.config.AutoConfig;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import com.suke.zhjg.common.autofull.entity.OssEntity;
import com.suke.zhjg.common.autofull.handler.*;
import com.suke.zhjg.common.autofull.interceptor.SqlInterceptor;
import com.suke.zhjg.common.autofull.resolver.AutoDecodeMaskParamResolver;
import com.suke.zhjg.common.autofull.sequence.AutoSequence;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author czx
 * @title: AutoFullConfiguration
 * @projectName zhjg-common-autofull
 * @description: TODO
 * @date 2020/11/2514:17
 */
@Configuration
@EnableConfigurationProperties({OssEntity.class, ConfigProperties.class })
public class AutoFullConfiguration {

    @Bean
    public AutoSequence autoSequence(){
        return AutoSequence.init();
    }

    @Bean
    public SqlInterceptor sqlInterceptor(){
        return new SqlInterceptor();
    }

    @Bean
    public AutoFullAspect autoFullAspect(){
        return new AutoFullAspect();
    }

    @Bean
    public AutoDecodeMaskParamResolver autoDecodeMaskResolver(){
        return new AutoDecodeMaskParamResolver();
    }

    @Bean
    public ApplicationContextRegister applicationContextRegister(){
        return new ApplicationContextRegister();
    }

    @Bean
    public AutoConfig autoConfig(){
        return new AutoConfig();
    }

    @Bean
    public AutoFullMaskService autoFullMaskService(){
        return new AutoFullMaskService();
    }

    @Bean
    public AutoFullJoinService autoFullJoinService(){
        return new AutoFullJoinService();
    }

    @Bean
    @ConditionalOnMissingBean(OssEntity.class)
    public AutoFullOssURLService autoFullOssURLService(){
        return new AutoFullOssURLService();
    }

    @Bean
    public AutoFullFieldSQLService autoFullFieldSQLService(){
        return new AutoFullFieldSQLService();
    }

    @Bean
    public AutoFullFieldService autoFullFieldService(){
        return new AutoFullFieldService();
    }

    @Bean
    public AutoFullBeanService autoFullBeanService(){
        return new AutoFullBeanService();
    }

    @Bean
    public AutoFullBeanSQLService autoFullBeanSQLService(){
        return new AutoFullBeanSQLService();
    }

    @Bean
    public AutoFullListService autoFullListService(){
        return new AutoFullListService();
    }

    @Bean
    public AutoFullListSQLService autoFullListSQLService(){
        return new AutoFullListSQLService();
    }
}
