package com.suke.zhjg.common.autofull.sequence;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author czx
 * @title: AutoSequence
 * @projectName zhjg
 * @description: TODO 发号器
 * @date 2021/1/813:53
 */
@Component
public class AutoSequence {

    @Autowired
    public RedisTemplate redisTemplate;

    public static AutoSequence autoSequence;

    public static AutoSequence init(){
        if(autoSequence == null){
            autoSequence =  new AutoSequence();;
        }
        return autoSequence;
    }

    public Object get(String key){
        if(StrUtil.isEmpty(key)){
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    public String put(Object value){
        String sequence = getSequence();
        redisTemplate.opsForValue().set(sequence,value);
        return sequence;
    }

    public String put(String key,Object value){
        redisTemplate.opsForValue().set(key,value);
        return key;
    }

    public void remove(String key){
        redisTemplate.delete(key);
    }

    public String getSequence(){
        return SecureUtil.md5(UUID.randomUUID().toString() + System.currentTimeMillis());
    }
}
