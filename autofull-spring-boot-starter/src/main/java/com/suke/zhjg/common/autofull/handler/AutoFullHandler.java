package com.suke.zhjg.common.autofull.handler;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import com.suke.zhjg.common.autofull.config.AutoConfig;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

/**
 * @author czx
 * @title: AutoFullHandler
 * @projectName zhjg
 * @description: TODO 自动填充属性
 * @date 2020/8/2014:21
 */
@Slf4j
@UtilityClass
public class AutoFullHandler {

    private Handler handler;

    private static final int DEFAULT_LEVEL = 1;

    /**
     * 填充mybatis plus page对象的数据
     **/
    public <T> IPage<T> full(IPage<T> iPage) {
        if (CollUtil.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, null, DEFAULT_LEVEL, true)));
        }
        return iPage;
    }

    /**
     * 填充mybatis plus page对象的数据
     *
     * @param enableCache 是否使用缓存
     **/
    public <T> IPage<T> full(IPage<T> iPage, boolean enableCache) {
        if (CollUtil.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, null, DEFAULT_LEVEL, enableCache)));
        }
        return iPage;
    }

    /**
     * 填充mybatis plus page对象的数据
     * 填充时设置了当次方法的序列号
     **/
    public <T> IPage<T> full(IPage<T> iPage, String sequence) {
        if (CollUtil.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, sequence, DEFAULT_LEVEL, true)));
        }
        return iPage;
    }

    /**
     * 填充mybatis plus page对象的数据
     * 填充时设置了当次方法的序列号
     *
     * @param enableCache 是否使用缓存
     **/
    public <T> IPage<T> full(IPage<T> iPage, String sequence, boolean enableCache) {
        if (CollUtil.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, sequence, DEFAULT_LEVEL, enableCache)));
        }
        return iPage;
    }

    /**
     * 填充mybatis plus page对象的数据
     *
     * @param sequence 填充时设置了当次方法的序列号
     * @param level    填充时设置了填充层级
     **/
    public <T> IPage<T> full(IPage<T> iPage, String sequence, int level) {
        if (CollUtil.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, sequence, level, true)));
        }
        return iPage;
    }

    /**
     * 填充mybatis plus page对象的数据
     *
     * @param sequence    填充时设置了当次方法的序列号
     * @param level       填充时设置了填充层级
     * @param enableCache 是否使用缓存
     **/
    public <T> IPage<T> full(IPage<T> iPage, String sequence, int level, boolean enableCache) {
        if (CollUtil.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, sequence, level, enableCache)));
        }
        return iPage;
    }

    /**
     * 填充List对象的数据
     **/
    public <T> List<T> full(List<T> list) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, null, DEFAULT_LEVEL, true)));
        }
        return list;
    }

    /**
     * 填充List对象的数据
     *
     * @param enableCache 是否使用缓存
     **/
    public <T> List<T> full(List<T> list, boolean enableCache) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, null, DEFAULT_LEVEL, enableCache)));
        }
        return list;
    }

    /**
     * 填充List对象的数据
     *
     * @param sequence 填充时设置了当次方法的序列号
     **/
    public <T> List<T> full(List<T> list, String sequence) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, sequence, DEFAULT_LEVEL, true)));
        }
        return list;
    }

    /**
     * 填充List对象的数据
     *
     * @param sequence    填充时设置了当次方法的序列号
     * @param enableCache 是否使用缓存
     **/
    public <T> List<T> full(List<T> list, String sequence, boolean enableCache) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, sequence, DEFAULT_LEVEL, enableCache)));
        }
        return list;
    }

    /**
     * 填充mybatis plus page对象的数据
     *
     * @param sequence 填充时设置了当次方法的序列号
     * @param level    填充时设置了填充层级
     **/
    public <T> List<T> full(List<T> list, String sequence, int level) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, sequence, level, true)));
        }
        return list;
    }

    /**
     * 填充mybatis plus page对象的数据
     *
     * @param sequence    填充时设置了当次方法的序列号
     * @param level       填充时设置了填充层级
     * @param enableCache 是否使用缓存
     **/
    public <T> List<T> full(List<T> list, String sequence, int level, boolean enableCache) {
        if (CollUtil.isNotEmpty(list)) {
            list.forEach(obj -> BeanUtil.copyProperties(obj, handler(obj, sequence, level, enableCache)));
        }
        return list;
    }

    /**
     * 填充bean对象的数据
     **/
    public <T> T full(T entity) {
        if (ObjectUtil.isNotNull(entity)) {
            BeanUtil.copyProperties(entity, handler(entity, null, DEFAULT_LEVEL, true));
        }
        return entity;
    }

    /**
     * 填充bean对象的数据
     *
     * @param enableCache 是否使用缓存
     **/
    public <T> T full(T entity, boolean enableCache) {
        if (ObjectUtil.isNotNull(entity)) {
            BeanUtil.copyProperties(entity, handler(entity, null, DEFAULT_LEVEL, enableCache));
        }
        return entity;
    }

    /**
     * 填充bean对象的数据
     *
     * @param sequence 填充时设置了当次方法的序列号
     **/
    public <T> T full(T entity, String sequence) {
        if (ObjectUtil.isNotNull(entity)) {
            BeanUtil.copyProperties(entity, handler(entity, sequence, DEFAULT_LEVEL, true));
        }
        return entity;
    }

    /**
     * 填充bean对象的数据
     *
     * @param sequence    填充时设置了当次方法的序列号
     * @param enableCache 是否使用缓存
     **/
    public <T> T full(T entity, String sequence, boolean enableCache) {
        if (ObjectUtil.isNotNull(entity)) {
            BeanUtil.copyProperties(entity, handler(entity, sequence, DEFAULT_LEVEL, enableCache));
        }
        return entity;
    }

    /**
     * 填充bean对象的数据
     *
     * @param sequence 填充时设置了当次方法的序列号
     * @param level    填充时设置了填充层级
     **/
    public <T> T full(T entity, String sequence, int level) {
        if (ObjectUtil.isNotNull(entity)) {
            BeanUtil.copyProperties(entity, handler(entity, sequence, level, true));
        }
        return entity;
    }

    /**
     * 填充bean对象的数据
     *
     * @param sequence    填充时设置了当次方法的序列号
     * @param level       填充时设置了填充层级
     * @param enableCache 是否使用缓存
     **/
    public <T> T full(T entity, String sequence, int level, boolean enableCache) {
        if (ObjectUtil.isNotNull(entity)) {
            BeanUtil.copyProperties(entity, handler(entity, sequence, level, enableCache));
        }
        return entity;
    }

    /**
     * 开始处理填充
     * 根据注解找到对应的处理类进行处理
     **/
    protected Object handler(Object obj, String sequence, int level, boolean enableCache) {
        Field[] fields = obj.getClass().getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                Annotation[] annotations = field.getDeclaredAnnotations();
                for (Annotation annotation : annotations) {
                    AutoConfig autoConfig = ApplicationContextRegister.getApplicationContext().getBean(AutoConfig.class);
                    if (ObjectUtil.isNotNull(autoConfig)) {
                        handler = (Handler) autoConfig.findBean(annotation);
                    }
                    if (handler != null) {
                        handler.result(annotation, fields, field, obj, sequence, level, enableCache);
                    }
                }
            }
        }
        return obj;
    }
}
