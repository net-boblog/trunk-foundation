package com.tumbleweed.platform.trunk.scheduler.base;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by mylover on 1/28/16.
 */
public class ThreadPoolManager {
    private ThreadPoolExecutor executor;
    private int corePoolSize = 16;
    private int maximumPoolSize = 128;
    private int keepAliveTime = 60;
    private BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue(128);

    public ThreadPoolManager(int corePoolSize, int maxPoolSize, int keepAliveTime) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maxPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize,
                (long)keepAliveTime, TimeUnit.SECONDS, this.workQueue, new ThreadPoolExecutor.AbortPolicy());
    }

    public long getTaskCount() {
        return this.executor.getTaskCount();
    }

    public int getPoolSize() {
        return this.executor.getPoolSize();
    }

    public int getActiveCount() {
        return this.executor.getActiveCount();
    }

    public void close() {
        this.executor.shutdown();
    }

    public void executeTask(Runnable task) {
        this.executor.execute(task);
    }

    public int getCorePoolSize() {
        return this.corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public int getMaximumPoolSize() {
        return this.maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public int getKeepAliveTime() {
        return this.keepAliveTime;
    }

    public void setKeepAliveTime(int keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }
}
