package com.suke.zhjg.common.autofull.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoFullField
 * @projectName zhjg
 * @description: TODO 自动填充字段 拼接      value = "湖南省长沙市岳麓区{park}5栋{number}"
 * @date 2020/8/2114:51
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFullJoin {

    String value() default "";

}
