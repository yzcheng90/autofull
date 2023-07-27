package com.suke.autofull.example.config;

import cn.hutool.core.collection.CollUtil;
import com.suke.zhjg.common.autofull.cache.AutoFullCacheDelete;
import io.lettuce.core.KeyScanCursor;
import io.lettuce.core.ScanArgs;
import io.lettuce.core.ScanCursor;
import io.lettuce.core.api.async.RedisKeyAsyncCommands;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 自定义删除key方法
 */
@Slf4j
//@Component
public class CustomAutoFullCacheDelete implements AutoFullCacheDelete {
    @Override
    public Set<String> getDeleteKeys(RedisTemplate<String, Object> redisTemplate, String tableName) {
        log.info("自定义");
        String patternKey = "*" + tableName + "*";
        Set<String> keys = redisTemplate.execute((RedisConnection connection) -> {
            Set<String> keySet = CollUtil.newHashSet();
            //定义起始游标，获取lettuce原生引用，定义scan参数
            ScanCursor scanCursor = ScanCursor.INITIAL;
            RedisKeyAsyncCommands commands = (RedisKeyAsyncCommands) connection.getNativeConnection();
            ScanArgs scanArgs = ScanArgs.Builder.limit(1000).match(patternKey);
            try {
                do {
                    //最少scan一次，当返回不为空时将扫描到的key添加到统一key列表中
                    KeyScanCursor<byte[]> keyScanCursor = (KeyScanCursor) commands.scan(scanCursor, scanArgs).get();
                    if (keyScanCursor != null) {
                        if (CollUtil.isNotEmpty(keyScanCursor.getKeys())) {
                            keyScanCursor.getKeys().forEach(b -> keySet.add(new String(b)));
                        }
                        scanCursor = keyScanCursor;
                    } else {
                        scanCursor = ScanCursor.FINISHED;
                    }
                } while (!scanCursor.isFinished());
            } catch (Exception e) {
                log.error("redisClient scanKey fail patternKey:[{}]", patternKey, e);
            }
            return keySet;
        });
        return keys;
    }
}
