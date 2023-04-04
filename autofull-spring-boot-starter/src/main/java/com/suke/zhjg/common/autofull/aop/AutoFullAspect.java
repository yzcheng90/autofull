package com.suke.zhjg.common.autofull.aop;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suke.zhjg.common.autofull.annotation.AutoFullData;
import com.suke.zhjg.common.autofull.handler.AutoFullHandler;
import com.suke.zhjg.common.autofull.sequence.AutoSequence;
import com.suke.zhjg.common.autofull.util.R;
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

    private static final int DEFAULT_LEVEL = 1;

    private static final String DATA = "data";

    @Pointcut("@annotation(com.suke.zhjg.common.autofull.annotation.AutoFullData)")
    public void autoFullDataPointCut() {

    }

    @Around(value = "@annotation(autoFullData)", argNames = "point,autoFullData")
    public Object around(ProceedingJoinPoint point, AutoFullData autoFullData) throws Throwable {
        int maxLevel = autoFullData.maxLevel();
        boolean enableCache = autoFullData.enableCache();
        String sequence = AutoSequence.init().put(maxLevel == 0 ? DEFAULT_LEVEL : maxLevel);
        Object proceed = point.proceed();
        Class<?> cls = proceed.getClass();
        if (cls.equals(R.class)) {
            R result = (R) proceed;
            if (result != null) {
                Object data = result.get(DATA);
                if (data != null) {
                    cls = data.getClass();
                    data = this.fullData(cls, data, sequence, maxLevel, enableCache);
                    result.put(DATA, data);
                    return result;
                }
            }
        } else {
            return this.fullData(cls, proceed, sequence, maxLevel, enableCache);
        }
        return proceed;
    }


    public Object fullData(Class<?> cls, Object data, String sequence, int maxLevel, boolean enableCache) {
        if (cls.equals(ArrayList.class)) {
            ArrayList listData = (ArrayList) data;
            if (maxLevel == 0) {
                AutoFullHandler.full(listData, sequence, enableCache);
            } else {
                AutoFullHandler.full(listData, sequence, DEFAULT_LEVEL, enableCache);
            }
        } else if (cls.equals(Page.class)) {
            IPage pageData = (IPage) data;
            if (maxLevel == 0) {
                AutoFullHandler.full(pageData, sequence, enableCache);
            } else {
                AutoFullHandler.full(pageData, sequence, DEFAULT_LEVEL, enableCache);
            }
        } else {
            if (maxLevel == 0) {
                AutoFullHandler.full(data, sequence, enableCache);
            } else {
                AutoFullHandler.full(data, sequence, DEFAULT_LEVEL, enableCache);
            }
        }
        AutoSequence.init().remove(sequence);
        return data;
    }
}
