package com.suke.zhjg.common.autofull.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoFullBean
 * @projectName zhjg
 * @description: TODO 自动填充Bean
 * @date 2020/8/2114:51
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFullBean {

    /**
     * 表名
     **/
    String table() default "";

    /**
     * 条件字段
     **/
    String conditionField() default "";

    /**
     * 是否支持查询子级
     **/
    boolean childLevel() default false;

    /**
     * 是否使用缓存
     * 默认为true
     **/
    boolean useCache() default true;
}
