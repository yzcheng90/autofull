package com.suke.zhjg.common.autofull.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoFullBeanSQL
 * @projectName zhjg
 * @description: TODO 自动填充Bean sql
 * @date 2020/8/2114:51
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFullBeanSQL {

    /**
     * sql
     **/
    String sql() default "";

    /**
     * 是否支持查询子级
     **/
    boolean childLevel() default false;
}
