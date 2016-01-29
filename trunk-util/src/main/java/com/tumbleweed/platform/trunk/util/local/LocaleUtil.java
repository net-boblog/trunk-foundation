package com.tumbleweed.platform.trunk.util.local;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Created by mylover on 1/29/16.
 */
public final class LocaleUtil {

    public static Locale getCurrentLocale(HttpServletRequest request) {
        if (request.getLocale() != null) {
            if (request.getLocale().toString().toLowerCase().contains("hans")) {
                return Locale.SIMPLIFIED_CHINESE;
            }

            return request.getLocale();
        }

        if (request.getSession() != null) {
            return (Locale) request.getSession(false).getAttribute("locale");
        }

        return Locale.getDefault();
    }

    public static Set<Locale> getSupportedLocales() {
        Set<Locale> supportedLocales = new HashSet<Locale>();
        supportedLocales.add(Locale.SIMPLIFIED_CHINESE);
        supportedLocales.add(Locale.US);

        return supportedLocales;
    }

    public static boolean isSupportedLocale(Locale locale) {
        Set<Locale> supportedLocales = getSupportedLocales();
        return supportedLocales.contains(locale);
    }

    public static boolean isSupportedLocale(String locale) {
        Set<Locale> supportedLocales = getSupportedLocales();

        for (Locale supportedLocale : supportedLocales) {
            if (supportedLocale.toString().equals(locale)) {
                return true;
            }
        }

        return false;
    }

    public static Locale transformLocale(Locale locale) {
        if (locale != null && locale.toString().toLowerCase().contains("hans")) {
            return Locale.SIMPLIFIED_CHINESE;
        } else {
            return locale;
        }
    }

}
