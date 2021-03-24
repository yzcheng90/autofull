package com.suke.zhjg.common.autofull.annotation;


import com.suke.zhjg.common.autofull.config.MaskType;

import java.lang.annotation.*;

/**
 * @author Administrator
 * @title: AutoFullMask
 * @projectName zhjg-common-autofull
 * @description: TODO 数据脱敏
 * @date 2020/11/2514:08
 */
@Documented
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFullMask {

    MaskType type() default MaskType.idCard;

}
