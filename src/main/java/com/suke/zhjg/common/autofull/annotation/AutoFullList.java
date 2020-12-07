package com.suke.zhjg.common.autofull.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoFullField
 * @projectName zhjg
 * @description: TODO 自动填充List
 * @date 2020/8/2114:51
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFullList {

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

}
