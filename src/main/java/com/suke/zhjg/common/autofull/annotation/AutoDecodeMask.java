package com.suke.zhjg.common.autofull.annotation;

import java.lang.annotation.*;

/**
 * @author czx
 * @title: AutoDecodeMask
 * @projectName zhjg
 * @description: TODO 脱敏数据解密
 * @date 2021/1/2810:13
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoDecodeMask {
}
