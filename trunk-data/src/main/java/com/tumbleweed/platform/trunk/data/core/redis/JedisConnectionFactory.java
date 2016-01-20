package com.tumbleweed.platform.trunk.data.core.redis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.util.Pool;

/**
 * Created by mylover on 1/20/16.
 */
public class JedisConnectionFactory implements InitializingBean, DisposableBean, RedisConnectionFactory {

    private static final Log log = LogFactory.getLog(JedisConnectionFactory.class);
    private JedisShardInfo shardInfo;
    private String hostName = "localhost";
    private int port = 6379;
    private int timeout = 2000;
    private String password;
    private boolean usePool = true;
    private JedisPool pool = null;
    private JedisPoolConfig poolConfig = new JedisPoolConfig();
    private int dbIndex = 0;

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return false;
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return null;
    }

    public JedisConnectionFactory() {
        log.info("JedisConnectionFactory: " + this.dbIndex);
    }

    public JedisConnectionFactory(JedisShardInfo shardInfo) {
        this.shardInfo = shardInfo;
    }

    public JedisConnectionFactory(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    protected Jedis fetchJedisConnector() {
        try {
            if(this.usePool && this.pool != null) {
                return (Jedis)this.pool.getResource();
            } else {
                Jedis ex = new Jedis(this.getShardInfo());
                ex.connect();
                return ex;
            }
        } catch (Exception var2) {
            throw new RedisConnectionFailureException("Cannot get Jedis connection", var2);
        }
    }

    protected JedisConnection postProcessConnection(JedisConnection connection) {
        return connection;
    }

    public void afterPropertiesSet() {
        if(this.shardInfo == null) {
            this.shardInfo = new JedisShardInfo(this.hostName, this.port);
            if(StringUtils.hasLength(this.password)) {
                this.shardInfo.setPassword(this.password);
            }

            if(this.timeout > 0) {
                this.shardInfo.setSoTimeout(this.timeout);
            }
        }

        if(this.usePool) {
            this.pool = new JedisPool(this.poolConfig, this.shardInfo.getHost(),
                    this.shardInfo.getPort(), this.shardInfo.getSoTimeout(), this.shardInfo.getPassword());
        }

    }

    public void destroy() {
        if(this.usePool && this.pool != null) {
            try {
                this.pool.destroy();
            } catch (Exception var2) {
                log.warn("Cannot properly close Jedis pool", var2);
            }

            this.pool = null;
        }

    }

    public JedisConnection getConnection() {
        Jedis jedis = this.fetchJedisConnector();
        return this.postProcessConnection(this.usePool?new JedisConnection(jedis, this.pool,
                this.dbIndex):new JedisConnection(jedis, (Pool)null, this.dbIndex));
    }

    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return JedisUtils.convertJedisAccessException(ex);
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public JedisShardInfo getShardInfo() {
        return this.shardInfo;
    }

    public void setShardInfo(JedisShardInfo shardInfo) {
        this.shardInfo = shardInfo;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean getUsePool() {
        return this.usePool;
    }

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }

    public JedisPoolConfig getPoolConfig() {
        return this.poolConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public int getDatabase() {
        return this.dbIndex;
    }

    public void setDatabase(int index) {
        Assert.isTrue(index >= 0, "invalid DB index (a positive index required)");
        this.dbIndex = index;
    }

    public int getDbIndex() {
        return this.dbIndex;
    }

    public void setDbIndex(int dbIndex) {
        this.dbIndex = dbIndex;
    }
}
