package com.tumbleweed.platform.trunk.data.core.redis;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by mylover on 1/20/16.
 */
public class DynamicRedisSelector {
    private static final Logger log = LogManager.getLogger(DynamicRedisSelector.class);
    public static final String KEY_REDIS = "KEY_REDIS";
    public static final String REDIS_MASTER = "REDIS_MASTER";
    public static final String REDIS_SLAVE = "REDIS_SLAVE";
    private static ConcurrentMap<String, Boolean> contextHolder = new ConcurrentHashMap();
    private RedisTemplate<Serializable, Serializable> redisTemplate0;
    private RedisTemplate<Serializable, Serializable> redisTemplate1;

    public DynamicRedisSelector() {
        contextHolder.put("KEY_REDIS", Boolean.valueOf(true));
        log.info("init Redis of Master...");
    }

    public RedisTemplate<Serializable, Serializable> selector() {
        boolean selector = ((Boolean)contextHolder.get("KEY_REDIS")).booleanValue();
        log.debug("get selector: " + selector);
        return selector?this.redisTemplate0:this.redisTemplate1;
    }

    public static String switchContext() {
        boolean current = ((Boolean)contextHolder.remove("KEY_REDIS")).booleanValue();
        current = !current;
        contextHolder.put("KEY_REDIS", Boolean.valueOf(current));
        String after = current?"REDIS_MASTER":"REDIS_SLAVE";
        log.info("switch [" + after + "] Redis...");
        return after;
    }

    public RedisTemplate<Serializable, Serializable> getRedisTemplate0() {
        return this.redisTemplate0;
    }

    public void setRedisTemplate0(RedisTemplate<Serializable, Serializable> redisTemplate0) {
        this.redisTemplate0 = redisTemplate0;
    }

    public RedisTemplate<Serializable, Serializable> getRedisTemplate1() {
        return this.redisTemplate1;
    }

    public void setRedisTemplate1(RedisTemplate<Serializable, Serializable> redisTemplate1) {
        this.redisTemplate1 = redisTemplate1;
    }
}
