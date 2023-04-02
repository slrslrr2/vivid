package com.vivid.dream.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisLockUtils {
    private static final long TRY_TIME = 100;

    /***
     * @param redisTemplate
     * @param key
     * @param lockExpireTime ms
     * @return
     */
    public static boolean tryLock(RedisTemplate redisTemplate, String key, long lockExpireTime) {
        try {
            long expireAt = System.currentTimeMillis() + lockExpireTime;
            boolean lock = redisTemplate.opsForValue().setIfAbsent(key, expireAt + "", lockExpireTime, TimeUnit.MILLISECONDS);
            if (lock) {
                return true;
            }

            // 만료
            String expectExpireStr = (String) redisTemplate.opsForValue().get(key);
            if (StringUtils.isBlank(expectExpireStr)) {
                return false;
            }

            Long expectExpire = Long.parseLong(expectExpireStr);
            if (System.currentTimeMillis() > expectExpire) {
                redisTemplate.delete(key);
                return tryLock(redisTemplate, key, lockExpireTime);
            }

        } catch (Exception e) {
            log.warn(" getLock exception:{}", key, e);
        }
        return false;
    }

    /***
     * @param redisTemplate
     * @param key
     */
    public static void releaseLock(RedisTemplate redisTemplate, String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error(" releaseLock exception:{}", key, e);
        }
    }

}
