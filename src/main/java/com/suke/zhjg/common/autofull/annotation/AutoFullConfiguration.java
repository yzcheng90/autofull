package com.suke.zhjg.common.autofull.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoFullConfiguration
 * @projectName zhjg
 * @description: TODO
 * @date 2020/9/711:18
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFullConfiguration {

    Class type();

}
