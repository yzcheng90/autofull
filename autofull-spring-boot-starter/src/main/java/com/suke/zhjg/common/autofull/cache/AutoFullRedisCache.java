package com.suke.zhjg.common.autofull.cache;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suke.zhjg.common.autofull.config.ApplicationContextRegister;
import com.suke.zhjg.common.autofull.constant.ConstantBeans;
import com.suke.zhjg.common.autofull.entity.ConfigProperties;
import com.suke.zhjg.common.autofull.util.SQLTableUtil;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author czx
 * @title: AutoFullRedisCache
 * @projectName zhjg
 * @description: TODO
 * @date 2021/3/1716:19
 */
@Slf4j
@UtilityClass
public class AutoFullRedisCache {

    // 有效期（7天）
    private static final int expireTime = 7 * 24 * 60;

    private RedisTemplate<String, Object> getRedisTemplate() {
        RedisTemplate<String, Object> redisTemplate = (RedisTemplate<String, Object>) ApplicationContextRegister.getApplicationContext().getBean("redisTemplate");
        return redisTemplate;
    }

    public StringRedisTemplate getStringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = (StringRedisTemplate) ApplicationContextRegister.getApplicationContext().getBean("stringRedisTemplate");
        return stringRedisTemplate;
    }

    public ConfigProperties getConfigProperties() {
        return ApplicationContextRegister.getApplicationContext().getBean(ConfigProperties.class);
    }

    private ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = ApplicationContextRegister.getApplicationContext().getBean(ObjectMapper.class);
        return objectMapper;
    }

    private AutoFullCacheDelete getAutoFullCacheDelete() {
        AutoFullCacheDelete cacheDelete = ApplicationContextRegister.getApplicationContext().getBean(AutoFullCacheDelete.class);
        return cacheDelete;
    }

    private String getKey(String sql, Object param) {
        String paramStr = "";
        if (ObjectUtil.isNotNull(param)) {
            try {
                paramStr = getObjectMapper().writeValueAsString(param);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        String cachePrefix = getConfigProperties().getCachePrefix();
        return cachePrefix + ConstantBeans.cacheName + SecureUtil.md5(sql + paramStr);
    }

    public <T> List<T> getList(String ID, String sql, Object param, T t) {
        String key = getKey(sql, param);
        Object data = getRedisTemplate().opsForValue().get(key);
        if (ObjectUtil.isNotNull(data)) {
            if (getConfigProperties().isShowLog()) {
                log.info("ID:{},取缓存数据,key:{}", ID, key);
            }
            return (List<T>) data;
        }
        return null;
    }

    public String getStringData(String ID, String sql, Object param) {
        String key = getKey(sql, param);
        Object data = getRedisTemplate().opsForValue().get(key);
        if (ObjectUtil.isNotNull(data)) {
            if (getConfigProperties().isShowLog()) {
                log.info("ID:{},取缓存数据,key:{}", ID, key);
            }
            return String.valueOf(data);
        }
        return null;
    }

    public void setData(String ID, String sql, Object param, Object data) {
        if (ObjectUtil.isNull(data)) {
            return;
        }
        String cachePrefix = getConfigProperties().getCachePrefix();
        List<String> tableName = SQLTableUtil.getSelectTableName(sql);
        String key = getKey(sql, param);
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 保存 每个表 + key
        tableName.forEach(name -> {
            String tableKey = cachePrefix + ConstantBeans.cacheName + name + key;
            if (getConfigProperties().isShowLog()) {
                log.info("ID:{},保存缓存key：{}", ID, tableKey);
            }
            valueOperations.set(tableKey, key, expireTime, TimeUnit.MINUTES);
        });
        // 保存 key 和数据
        valueOperations.set(key, data, expireTime, TimeUnit.MINUTES);
        if (getConfigProperties().isShowLog()) {
            log.info("ID:{},保存缓存key：{}", ID, key);
        }
    }

    public void deleteData(String ID, String tableName) {
        RedisTemplate<String, Object> redisTemplate = getRedisTemplate();
        StringRedisTemplate stringRedisTemplate = getStringRedisTemplate();
        AutoFullCacheDelete autoFullCacheDelete = getAutoFullCacheDelete();
        String cachePrefix = getConfigProperties().getCachePrefix();
        tableName = cachePrefix + ConstantBeans.cacheName + tableName + "??";
        Set<String> keys = autoFullCacheDelete.getDeleteKeys(redisTemplate, tableName);
        if (CollUtil.isNotEmpty(keys)) {
            List<String> list = keys.stream().collect(Collectors.toList());
            // 删除所有的key
            list.forEach(key -> {
                Object keyData = redisTemplate.opsForValue().get(key);
                if (ObjectUtil.isNotNull(keyData)) {
                    // 删除 key 对应的数据
                    redisTemplate.delete(keyData.toString());
                    if (getConfigProperties().isShowLog()) {
                        log.info("ID:{},删除缓存keyData：{}", ID, keyData);
                    }
                }
                stringRedisTemplate.delete(key);
                if (getConfigProperties().isShowLog()) {
                    log.info("ID:{},删除缓存keyData：{}", ID, key);
                }
            });
        }
    }

    public void deleteAll() {
        StringRedisTemplate stringRedisTemplate = getStringRedisTemplate();
        String cachePrefix = getConfigProperties().getCachePrefix();
        Set<String> keys = stringRedisTemplate.keys(cachePrefix + ConstantBeans.cacheName + "*");
        keys.forEach(key -> {
            if (getConfigProperties().isShowLog()) {
                log.info("删除缓存数据,key:{}", key);
            }
            stringRedisTemplate.delete(key);
        });
    }

}
