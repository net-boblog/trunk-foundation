package com.tumbleweed.platform.trunk.data.core.ehcache;

import net.sf.ehcache.*;
import net.sf.ehcache.event.CacheManagerEventListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;

/**
 * Created by mylover on 1/12/16.
 */
public class EhCacheManager implements CacheManagerEventListener {
    private static Log logger = LogFactory.getLog(EhCacheManager.class);

    private static EhCacheManager instance;
    private String ehcachePath = "ehcache.xml";
    private boolean isFullPath = false;
    private CacheManager cacheManager;

    public EhCacheManager() {
        instance = this;
    }

    public void postConstruct() {
        String fullPath = this.ehcachePath;
        if(!this.isFullPath) {
            String userDir = System.getProperty("user.dir");
            fullPath = userDir + File.separator + this.ehcachePath;
        }

        logger.info("fullPath:" + fullPath);
        this.cacheManager = CacheManager.create(fullPath);
        this.cacheManager.setCacheManagerEventListener(this);
        logger.info("cache manager finished...");
    }

    public static EhCacheManager getInstance() {
        return instance;
    }

    public Object getCacheData(String cacheName, String key) {
        try {
            Cache e = this.cacheManager.getCache(cacheName);
            if(e != null && e.get(key) != null) {
                return e.get(key).getValue();
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return null;
    }

    public void putDataInCache(String cacheName, String key, Object data) {
        try {
            Cache e = this.cacheManager.getCache(cacheName);
            if(e != null) {
                Element element = new Element(key, data);
                e.put(element);
                logger.info("write [" + key + "] into the cache...");
            } else {
                logger.warn("cache [{}] not exists...");
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }

    }

    public long getCacheMemoryCount(String cacheName) {
        try {
            Cache e = this.cacheManager.getCache(cacheName);
            if(e != null) {
                long count = e.getMemoryStoreSize();
                logger.info("object count [" + count + "] of [" + cacheName + "] cache...");
                return count;
            }

            logger.warn("cache [{}] not exists...");
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return -1L;
    }

    public long calCacheMemorySize(String cacheName) {
        try {
            Cache e = this.cacheManager.getCache(cacheName);
            if(e != null) {
                long size = e.calculateInMemorySize();
                logger.info("used memory size ["+Long.valueOf(size / 1000L)+"] KB of ["+cacheName+"] cache...");
                return size;
            }

            logger.warn("cache [{}] not exists...");
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        return -1L;
    }

    public void destroy() {
        if(this.cacheManager != null) {
            this.cacheManager.shutdown();
        }

    }

    public void dispose() throws CacheException {
    }

    public Status getStatus() {
        return null;
    }

    public void init() throws CacheException {
        logger.info("init...");
    }

    public void notifyCacheAdded(String cacheName) {
        logger.info("notifyCacheAdded:" + cacheName);
        Cache cache = this.cacheManager.getCache(cacheName);
        if(cache != null) {
            long count = cache.getStatistics().getMemoryStoreObjectCount();
            logger.info("MemoryStoreObjectCount:"+ count);
        }

    }

    public void notifyCacheRemoved(String cacheName) {
        logger.info("notifyCacheRemoved:" + cacheName);
        Cache cache = this.cacheManager.getCache(cacheName);
        if(cache != null) {
            long count = cache.getStatistics().getMemoryStoreObjectCount();
            logger.info("MemoryStoreObjectCount:"+ count);
        }

    }

    public boolean isFullPath() {
        return this.isFullPath;
    }

    public void setFullPath(boolean isFullPath) {
        this.isFullPath = isFullPath;
    }

    public String getEhcachePath() {
        return this.ehcachePath;
    }

    public void setEhcachePath(String ehcachePath) {
        this.ehcachePath = ehcachePath;
    }
}
