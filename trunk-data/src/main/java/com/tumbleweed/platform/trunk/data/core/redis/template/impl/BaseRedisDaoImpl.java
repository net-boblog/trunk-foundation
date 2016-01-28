/**
 *
 */
package com.tumbleweed.platform.trunk.data.core.redis.template.impl;

import com.tumbleweed.platform.trunk.base.exception.TrunkRedisException;
import com.tumbleweed.platform.trunk.data.core.redis.DynamicRedisSelector;
import com.tumbleweed.platform.trunk.data.core.redis.template.BaseRedisDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * base redis access api.
 *
 */
@Repository
public class BaseRedisDaoImpl implements BaseRedisDao {

    private static final Logger log = LogManager.getLogger(BaseRedisDaoImpl.class.getName());

    @Autowired
    private DynamicRedisSelector redisSelector;
    /**
     * 根据key查询redis上的value
     *
     * @param key
     *            key值
     * @return
     */
    @Override
    public String getRedisValue(final String key) throws TrunkRedisException {
        try {
            return redisSelector.selector().execute(new RedisCallback<String>() {

                @Override
                public String doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] bkey = redisSelector.selector().getStringSerializer().serialize(key);
                    if (connection.exists(bkey)) {
                        byte[] bvalue = connection.get(bkey);
                        String value = redisSelector.selector().getStringSerializer().deserialize(bvalue);
                        log.debug("getRedisValue key: {} value: {}", key, value);
                        return value;
                    }
                    return null;
                }
            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }

    /**
     * 根据key查询redis上的value
     *
     * @param keys
     *            key值
     * @return
     */
    public List<String> getRedisValue(final List<String> keys) throws TrunkRedisException {
        try {
            List<Object> list = redisSelector.selector().execute(new RedisCallback<List<Object>>() {
                @Override
                public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                    connection.openPipeline();
                    byte[] bkey = null;
                    for (String key : keys) {
                        bkey = redisSelector.selector().getStringSerializer().serialize(key);
                        connection.get(bkey);
                    }
                    return connection.closePipeline();
                }
            });

            String value = null;
            List<String> values = new ArrayList<String>();
            for (Object object : list) {
                if (object == null) {
                    values.add(null);
                    continue;
                }
                value = redisSelector.selector().getStringSerializer().deserialize((byte[]) object);
                values.add(value);
            }
            return values;
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }

    /**
     * 获取set集合值
     * @param key
     * @return
     * @throws TrunkRedisException
     */
    @Override
    public List<String> getValFromSortedSet(final String key) throws TrunkRedisException {
        try {
            return redisSelector.selector().execute(new RedisCallback<List<String>>() {
                @Override
                public List<String> doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] bKey = redisSelector.selector().getStringSerializer().serialize(key);
                    Set<byte[]> bValues = connection.zRange(bKey, 0, -1);
                    List<String> values = new ArrayList<String>();
                    for (byte[] bValue : bValues) {
                        String value = null;
                        if (bValue != null && bValue.length > 0) {
                            value = redisSelector.selector().getStringSerializer().deserialize(bValue);
                        }
                        values.add(value);
                    }
                    log.debug("getValFromSortedSet key: {} value: {}", key, values);
                    return values;
                }
            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }

    @Override
    public Set<String> getRedisForSet(final String key) throws TrunkRedisException {
        try {
            return redisSelector.selector().execute(new RedisCallback<Set<String>>() {
                @Override
                public Set<String> doInRedis(RedisConnection connection) throws DataAccessException {
                    Set<byte[]> result = connection.sMembers(redisSelector.selector().getStringSerializer().serialize(key));
                    Set<String> returnResult = new HashSet<String>();
                    if (result != null && !result.isEmpty()) {
                        for (byte[] value : result) {
                            returnResult.add(redisSelector.selector().getStringSerializer().deserialize(value));
                        }
                    }
                    return returnResult;
                }
            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }

    @Override
    public Object saveRedisForSet(final String key,final String value)
            throws TrunkRedisException {
        try {
            return redisSelector.selector().execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    byte[] keyByte = redisSelector.selector().getStringSerializer().serialize(key);
                    byte[] valueByte = redisSelector.selector().getStringSerializer().serialize(value);
                    connection.sAdd(keyByte, valueByte);
                    return null;
                }
            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }

    @Override
    public Object saveRedisForSet(final String key,final List<String> values)
            throws TrunkRedisException {
        try {
            return redisSelector.selector().execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    byte[] keyByte = redisSelector.selector().getStringSerializer().serialize(key);

                    byte[][] valueByte = new byte[values.size()][];
                    for (int i = 0; i < values.size(); i++) {
                        byte[] bKey = redisSelector.selector().getStringSerializer().serialize(values.get(i));
                        valueByte[i] = bKey;
                    }

                    connection.sAdd(keyByte, valueByte);
                    return null;
                }
            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }


    @Override
    public Object removeRedisForSet(final String key,final List<String> values)
            throws TrunkRedisException {
        try {
            return redisSelector.selector().execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {

                    byte[] keyByte = redisSelector.selector().getStringSerializer().serialize(key);

                    byte[][] valueByte = new byte[values.size()][];
                    for (int i = 0; i < values.size(); i++) {
                        byte[] bKey = redisSelector.selector().getStringSerializer().serialize(values.get(i));
                        valueByte[i] = bKey;
                    }

                    connection.sRem(keyByte, valueByte);
                    return null;
                }
            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }

    @Override
    public void deleteRedisValue(final String key) throws TrunkRedisException {
        try {
            redisSelector.selector().execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) {
                    Long del = connection.del(redisSelector.selector().getStringSerializer().serialize(key));
                    log.debug("deleteRedisValue key: " + key + " value: " + del);
                    return del;
                }
            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }

    @Override
    public void deleteRedisValue(final List<String> keys) throws TrunkRedisException {
        try {
            redisSelector.selector().execute(new RedisCallback<Object>() {
                public Object doInRedis(RedisConnection connection) {
                    connection.openPipeline();
                    byte[] bkey = null;
                    for (String key : keys) {
                        bkey = redisSelector.selector().getStringSerializer().serialize(key);
                        Long del = connection.del(bkey);
                        log.debug("deleteRedisValue key: " + key + " value: " + del);
                    }
                    connection.closePipeline();
                    return null;
                }
            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }


    public Long getRedisTTL(final String key) throws TrunkRedisException {
        try {
            return (Long) redisSelector.selector().execute(new RedisCallback<Object>() {

                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] bkey = redisSelector.selector().getStringSerializer().serialize(key);
                    Long ttl = connection.ttl(bkey);
                    log.debug("getRedisTTL key: " + key + " TTL: " + ttl);
                    return ttl;
                }

            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }


    /**
     * 保存redis数据
     *
     * @param key
     *            key值
     * @param value
     *            value值
     * @param expire
     *            过期时间，单位秒
     */
    public void saveRedisValue(final String key, final String value, final long expire) throws TrunkRedisException {
        try {
            redisSelector.selector().execute(new RedisCallback<Object>() {

                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] bkey = redisSelector.selector().getStringSerializer().serialize(key);
                    byte[] bvalue = redisSelector.selector().getStringSerializer().serialize(value);
                    connection.set(bkey, bvalue);
                    connection.expire(bkey, expire);
                    log.debug("saveRedisValue key: " + key + " value: " + value + " expire: " + expire);
                    return null;
                }

            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }

    /**
     * redis数据原子+1操作
     *
     * @param key
     *            key值
     */
    public Long incrRedis(final String key) throws TrunkRedisException {

        try {
            return (Long) redisSelector.selector().execute(new RedisCallback<Object>() {

                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] bkey = redisSelector.selector().getStringSerializer().serialize(key);
                    Long incr = connection.incr(bkey);
                    log.debug("incrRedis key: " + key + "value: " + incr);
                    return incr;
                }

            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }


    /**
     * 保存redis数据
     *
     * @param key
     *            key值
     * @param value
     *            value值
     */
    public void saveRedisValue(final String key, final String value) throws TrunkRedisException {
        try {
            redisSelector.selector().execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    byte[] bkey = redisSelector.selector().getStringSerializer().serialize(key);
                    byte[] bvalue = redisSelector.selector().getStringSerializer().serialize(value);
                    connection.set(bkey, bvalue);
                    log.debug("key: {}, value: {}", key, value);
                    return null;
                }

            });
        } catch (Exception e) {
            throw new TrunkRedisException(e);
        }
    }
}
