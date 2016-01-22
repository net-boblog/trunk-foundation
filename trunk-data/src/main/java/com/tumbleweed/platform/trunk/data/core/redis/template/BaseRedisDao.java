/**
 * 
 */
package com.tumbleweed.platform.trunk.data.core.redis.template;

import com.tumbleweed.platform.trunk.base.exception.TrunkRedisException;

import java.util.List;
import java.util.Set;

/**
 * @author mylover
 */
public interface BaseRedisDao {

	/**
	 * 根据key查询redis上的value
	 *
	 * @param key
	 *            key值
	 * @return
	 */
	String getRedisValue(String key) throws TrunkRedisException;

	/**
	 * 根据key查询redis上的value
	 *
	 * @param keys
	 *            key值集合
	 * @return
	 */
	List<String> getRedisValue(final List<String> keys) throws TrunkRedisException;


	/**
	 * 获取数据
	 * @param key
	 * @return
	 * @throws TrunkRedisException
	 */
	List<String> getValFromSortedSet(final String key) throws TrunkRedisException;


	/**
	 * 获取set集合
	 * @param key
	 * @return
	 * @throws TrunkRedisException
	 */
	Set<String> getRedisForSet(final String key) throws TrunkRedisException;

    /**
     * 保存set集合
     * @param key
     * @param value
     * @return
     * @throws TrunkRedisException
     */
    Object saveRedisForSet(final String key, final String value) throws TrunkRedisException;


    /**
	 * 保存set集合
	 * @param key
	 * @param value
	 * @return
	 * @throws TrunkRedisException
	 */
	Object saveRedisForSet(final String key, final List<String> value) throws TrunkRedisException;

	/**
	 * 删除set集合
	 * @param key
	 * @param value
	 * @return
	 * @throws TrunkRedisException
	 */
	Object removeRedisForSet(final String key, final List<String> value) throws TrunkRedisException;


	/**
	 * 删除redis 数据
	 *
	 * @param key
	 * @throws TrunkRedisException
	 */
	void deleteRedisValue(final String key) throws TrunkRedisException;


	/**
	 * 批量删除redis 数据
	 *
	 * @param keys
	 * @throws TrunkRedisException
	 */
	void deleteRedisValue(final List<String> keys) throws TrunkRedisException;

	/**
	 * 获取key过期时间
	 *
	 * @param key
	 * @throws TrunkRedisException
	 */
	Long getRedisTTL(final String key) throws TrunkRedisException;

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
	void saveRedisValue(String key, String value, long expire) throws TrunkRedisException;

	/**
	 * redis数据原子+1操作
	 *
	 * @param key
	 *            key值
	 */
	Long incrRedis(String key) throws TrunkRedisException;


    /**
     * 保存redis数据
     *
     * @param key
     *            key值
     * @param value
     *            value值
     */
    void saveRedisValue(String key, String value) throws TrunkRedisException;
}
