package com.suke.zhjg.common.autofull.cache;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;

public interface AutoFullCacheDelete {

    Set<String> getDeleteKeys(RedisTemplate<String,Object> redisTemplate,String tableName);
}
