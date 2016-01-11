package com.tumbleweed.platform.trunk.base.core.service;

import com.tumbleweed.platform.trunk.base.core.component.DataIntegrityChecker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataIntegrityCheckerManager implements Runnable {

    private Log log = LogFactory.getLog(DataIntegrityCheckerManager.class);

    private Thread checkThread;

    private List<DataIntegrityChecker> dataIntegrityCheckers = new ArrayList<DataIntegrityChecker>();

    private long interval = Long.getLong("trunk.data.integrity.check.interval", 1000 * 60 * 10).longValue();

    public DataIntegrityCheckerManager() {
        startThread();
    }

    public long getInterval() {
        return interval;
    }

    public void startThread() {
        checkThread = new Thread(this);
        checkThread.setDaemon(true);
        checkThread.start();
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public void addDataIntegrityChecker(DataIntegrityChecker o) {
        synchronized (dataIntegrityCheckers) {
            dataIntegrityCheckers.add(o);
        }
    }

    public Collection<DataIntegrityChecker> getDataIntegrityCheckers() {
        synchronized (dataIntegrityCheckers) {
            return new ArrayList<DataIntegrityChecker>(dataIntegrityCheckers);
        }
    }

    public void run() {
        log.info("Starting Data Integrity Check Thread (interval=" + interval + " ms)");

        while (true) {
            try {
                Thread.sleep(interval);

                log.info("--- Checking ---");
                synchronized (dataIntegrityCheckers) {
                    for (DataIntegrityChecker o : dataIntegrityCheckers) {
                        try {
                            log.info("[" + o + "] " + o.process());
                        } catch (Throwable e) {
                            log.error("Error in checking: " + e, e);
                        }
                    }
                }
                log.info("--- Check end ---");
            } catch (InterruptedException e) {
                log.warn("Check thread interrupted, shutting down.");
                break;
            } catch (Exception e) {
                log.warn("Error encountered while check data integrity", e);
            }
        }
    }
}
