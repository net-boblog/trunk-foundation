package com.tumbleweed.platform.trunk.scheduler.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.LinkedList;

/**
 * Created by mylover on 1/28/16.
 */
public final class Queue {
    public static final Logger log = LogManager.getLogger(Queue.class);
    private LinkedList<Object> sendList = new LinkedList();

    public Queue() {
    }

    public Object get() {
        try {
            LinkedList e = this.sendList;
            synchronized(this.sendList) {
                if(this.isEmpty()) {
                    log.debug("queue is empty, then wait next object.");
                    this.sendList.wait();
                }

                Object sp = this.sendList.removeFirst();
                log.debug("queue size: " + this.sendList.size());
                return sp;
            }
        } catch (Exception var4) {
            log.error(var4);
            return null;
        }
    }

    public void put(Object sp) {
        try {
            LinkedList e = this.sendList;
            synchronized(this.sendList) {
                this.sendList.add(sp);
                log.debug("add a new object.");
                this.sendList.notify();
            }
        } catch (Exception var4) {
            log.error(var4);
        }

    }

    private boolean isEmpty() {
        return this.sendList.isEmpty();
    }

    public void close() {
        try {
            LinkedList e = this.sendList;
            synchronized(this.sendList) {
                this.sendList.notifyAll();
            }

            log.debug("notify all wait thread...");
        } catch (Exception var3) {
            log.error(var3);
        }

    }
}
