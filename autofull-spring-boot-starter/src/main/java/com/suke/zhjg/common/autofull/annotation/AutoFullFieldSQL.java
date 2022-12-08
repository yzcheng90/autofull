package com.suke.zhjg.common.autofull.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoFullSQL
 * @projectName zhjg
 * @description: TODO 自动填充 自定义SQL
 * @date 2020/8/2114:51
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFullFieldSQL {

    /**
     * 自定义 SQL
     **/
    String sql() default "";

    /**
     * 是否使用缓存
     * 默认为true
     **/
    boolean useCache() default true;

}
