package com.tumbleweed.platform.trunk.scheduler.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by mylover on 1/28/16.
 */
public abstract class WorkQueue extends Thread {
    public static final Logger log = LogManager.getLogger(WorkQueue.class);
    private final Queue queue = new Queue();
    private boolean isStop = false;

    public WorkQueue() {
        this.start();
    }

    public final void run() {
        Thread.currentThread().setName("WorkQueueThread");
        log.info("@WorkQueue Thread Running.");

        while(!this.isStop) {
            try {
                Object e = this.queue.get();
                this.execute(e);
            } catch (Exception var2) {
                log.error("WorkQueueThread:", var2);
            }
        }

        log.info("@WorkQueue Thread Stoped.\r\n");
    }

    protected abstract void execute(Object var1) throws Exception;

    public final void put(Object sp) {
        if(sp != null) {
            this.queue.put(sp);
        }

    }

    public final void close() {
        this.isStop = true;
        if(this.queue != null) {
            this.queue.close();
        }

    }
}
