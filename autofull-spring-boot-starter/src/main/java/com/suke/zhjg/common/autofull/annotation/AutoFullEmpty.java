package com.suke.zhjg.common.autofull.annotation;

import com.suke.zhjg.common.autofull.constant.FullEmpty;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoFullField
 * @projectName zhjg
 * @description: TODO 自动填充字段为null
 * @date 2020/8/2114:51
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFullEmpty {

    FullEmpty type() default FullEmpty.EMPTY;

}
