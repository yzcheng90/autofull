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
public @interface AutoFullListSQL {

    /**
     * 表名
     **/
    String sql() default "";

}
