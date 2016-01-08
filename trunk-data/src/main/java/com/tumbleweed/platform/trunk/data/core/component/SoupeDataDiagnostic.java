package com.tumbleweed.platform.trunk.data.core.component;

import com.mittop.platform.soupe.base.core.component.DiagnosticObject;
import com.mittop.platform.soupe.base.core.component.DiagnosticsLogger;
import com.mittop.platform.soupe.common.util.PrintfFormat;
import com.mittop.platform.soupe.common.util.StringUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
public class SoupeDataDiagnostic implements DiagnosticObject {

    private PrintfFormat fmt = new PrintfFormat("%-80s %-6d %-6d %6d");
    private PrintfFormat hdr = new PrintfFormat("%-80s %-6s %-6s %6s");

    @Autowired
    public SoupeDataDiagnostic(DiagnosticsLogger diagnosticsLogger) {
        diagnosticsLogger.addDiagnosticObject(this);
    }

    public String getName() {
        return "EhCache Diagnostics";
    }

    public String getShortName() {
        return "ehcacheDiag";
    }

    private List<Cache> getSortedCaches() {
        List<Cache> res = getCaches();

        Collections.sort(res, new Comparator<Cache>() {
            public int compare(Cache c1, Cache c2) {
                return c1.getName().compareTo(c2.getName());
            }
        });
        return res;
    }

    public String getStatus() {
        String separator = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer(separator);
        Object[] fmtArgs = new Object[5];

        fmtArgs[0] = "Cache";
        fmtArgs[1] = "Size";
        fmtArgs[2] = "Hits";
        fmtArgs[3] = "Misses";
        buf.append(hdr.sprintf(fmtArgs)).append(separator);
        fmtArgs[0] = "=====";
        fmtArgs[1] = "====";
        fmtArgs[2] = "====";
        fmtArgs[3] = "=====";
        buf.append(hdr.sprintf(fmtArgs));

        for (Cache cache : getSortedCaches()) {
            fmtArgs[0] = StringUtil.dotProximate(cache.getName(), 80);
            fmtArgs[1] = new Integer(cache.getSize());
            fmtArgs[2] = new Long(cache.getStatistics().getCacheHits());
            fmtArgs[3] = new Long(cache.getStatistics().getCacheMisses());

            buf.append(separator).append(fmt.sprintf(fmtArgs));
        }

        return buf.toString();
    }

    public String getShortStatus() {
        return getStatus();
    }

    public String toString() {
        return "Ehcache";
    }

    private List<Cache> getCaches() {
        CacheManager cacheManager = CacheManager.getInstance();
        String[] caches = cacheManager.getCacheNames();
        List<Cache> res = new ArrayList<Cache>(caches.length);

        for (int i = 0; i < caches.length; i++) {
            res.add(cacheManager.getCache(caches[i]));
        }
        return res;
    }

}
