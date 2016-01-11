package com.tumbleweed.platform.trunk.base.core.component;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DiagnosticsLogger {

    private Log logger = LogFactory.getLog(DiagnosticsLogger.class);

    private List<DiagnosticObject> diagnosticObjects = new ArrayList<DiagnosticObject>();

    public void addDiagnosticObject(DiagnosticObject o) {
        synchronized (diagnosticObjects) {
            diagnosticObjects.add(o);
        }
    }

    @Scheduled(fixedRate = 300000, initialDelay = 60000)
    public void run() {
        try {
            logger.debug("--- DIAGNOSTICS ---");
            synchronized (diagnosticObjects) {
                for (DiagnosticObject o : diagnosticObjects) {
                    try {
                        logger.debug("[" + o + "] " + o.getShortStatus());
                    } catch (Throwable e) {
                        logger.error("Error in diagnostics: " + e, e);
                    }
                }
            }
        } catch (Exception e) {
            logger.warn("Error encountered while collecting diagnostics", e);
        }
    }
}
