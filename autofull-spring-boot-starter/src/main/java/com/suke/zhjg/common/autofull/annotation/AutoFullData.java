package com.suke.zhjg.common.autofull.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoFull
 * @projectName zhjg-common-autofull
 * @description:
 *  本注解支持查询List、page、bean
 * @date 2022/12/613:23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoFullData {

    /**
     * 查询子级
     * 如果当前查询的对象中的list或bean中，还有字段还需要查询List 或bean的填充，这时maxLevel 字段就决定了要查询几级
     * 默认 0 只查询当前对象一级
     **/
    int maxLevel() default 0;
}
