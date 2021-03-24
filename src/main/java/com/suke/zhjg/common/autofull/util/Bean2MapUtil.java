package com.suke.zhjg.common.autofull.util;

import cn.hutool.core.bean.BeanUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author gqfeng
 * @title: Bean2MapUtil
 * @projectName zhjg
 * @description: TODO
 * @date 2021/2/19
 */
@Slf4j
@UtilityClass
public class Bean2MapUtil {

    /**
     * 将 List<JavaBean>对象转化为List<Map>
     * @param beanList
     * @return
     * @throws Exception
     */
    public static <T> List<Map<String, Object>> convertListBean2ListMap(List<T> beanList){
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 0, n = beanList.size(); i < n; i++){
            Object bean = beanList.get(i);
            Map<String, Object> map = BeanUtil.beanToMap(bean);
            mapList.add(map);
        }
        return mapList;
    }

}
