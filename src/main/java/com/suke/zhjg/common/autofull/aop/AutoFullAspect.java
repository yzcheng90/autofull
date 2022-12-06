package com.suke.zhjg.common.autofull.aop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suke.zhjg.common.autofull.annotation.AutoFullData;
import com.suke.zhjg.common.autofull.handler.AutoFullHandler;
import com.suke.zhjg.common.autofull.sequence.AutoSequence;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

/**
 * @author czx
 * @title: AutoFullAspect
 * @projectName zhjg-common-autofull
 * @date 2022/12/613:24
 */
@Slf4j
@Aspect
@Configuration
public class AutoFullAspect {

    private static final int defaultLevel = 1;

    @Pointcut("@annotation(com.suke.zhjg.common.autofull.annotation.AutoFullData)")
    public void autoFullDataPointCut() {

    }

    @Around(value = "@annotation(autoFullData)", argNames = "point,autoFullData")
    public Object around(ProceedingJoinPoint point, AutoFullData autoFullData) throws Throwable {
        int maxLevel = autoFullData.maxLevel();
        String sequence = AutoSequence.init().put(maxLevel == 0 ? defaultLevel : maxLevel);
        Object proceed = point.proceed();
        Class<?> aClass = proceed.getClass();
        if (aClass.equals(ArrayList.class)) {
            ArrayList data = (ArrayList) proceed;
            if (maxLevel == 0) {
                AutoFullHandler.full(data, sequence);
            } else {
                AutoFullHandler.full(data, sequence, defaultLevel);
            }
        } else if (aClass.equals(Page.class)) {
            IPage data = (IPage) proceed;
            if (maxLevel == 0) {
                AutoFullHandler.full(data, sequence);
            } else {
                AutoFullHandler.full(data, sequence, defaultLevel);
            }
        } else {
            if (maxLevel == 0) {
                AutoFullHandler.full(proceed, sequence);
            } else {
                AutoFullHandler.full(proceed, sequence, defaultLevel);
            }
        }
        AutoSequence.init().remove(sequence);
        return proceed;
    }
}
