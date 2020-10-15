package com.suke.zhjg.common.autofull.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoFullField
 * @projectName zhjg
 * @description: TODO 自动填充字段
 * @date 2020/8/2114:51
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFullField {

    /**
     * 表名
     **/
    String table() default "";

    /**
     * 条件字段
     **/
    String conditionField() default "";

    /**
     * 查询字段
     **/
    String queryField() default "";

}
