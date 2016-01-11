package com.tumbleweed.platform.trunk.base.config;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.DefaultPropertiesPersister;
import org.springframework.util.PropertiesPersister;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

public class ReloadableResourceBundleMessageSource extends AbstractMessageSource
        implements ResourceLoaderAware {

    private static final String PROPERTIES_SUFFIX = ".properties";
    private static final String XML_SUFFIX = ".xml";
    private String[] baseNames = new String[0];
    private String defaultEncoding;
    private Properties fileEncodings;
    private boolean fallbackToSystemLocale = true;
    private long cacheMillis = -1;
    private PropertiesPersister propertiesPersister = new DefaultPropertiesPersister();
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    private final Map<String, Map<Locale, List<String>>> cachedFileNames = new HashMap<String, Map<Locale, List<String>>>();

    private final Map<String, PropertiesHolder> cachedProperties = new HashMap<String, PropertiesHolder>();

    private final Map<Locale, PropertiesHolder> cachedMergedProperties = new HashMap<Locale, PropertiesHolder>();

    public void setBaseName(String baseName) {
        setBaseNames(new String[]{baseName});
    }

    public void setBaseNames(String[] baseNames) {
        if (baseNames != null) {
            this.baseNames = new String[baseNames.length];
            for (int i = 0; i < baseNames.length; i++) {
                String baseName = baseNames[i];
                Assert.hasText(baseName, "Basename must not be empty");
                this.baseNames[i] = baseName.trim();
            }
        } else {
            this.baseNames = new String[0];
        }
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public void setFileEncodings(Properties fileEncodings) {
        this.fileEncodings = fileEncodings;
    }

    public void setFallbackToSystemLocale(boolean fallbackToSystemLocale) {
        this.fallbackToSystemLocale = fallbackToSystemLocale;
    }

    public void setCacheSeconds(int cacheSeconds) {
        this.cacheMillis = (cacheSeconds * 1000);
    }

    public void setPropertiesPersister(PropertiesPersister propertiesPersister) {
        this.propertiesPersister =
                (propertiesPersister != null ? propertiesPersister
                        : new DefaultPropertiesPersister());
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = (resourceLoader != null ? resourceLoader
                : new DefaultResourceLoader());
    }

    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
        if (this.cacheMillis < 0) {
            PropertiesHolder propHolder = getMergedProperties(
                    locale);
            String result = propHolder.getProperty(code);
            if (result != null) {
                return result;
            }
        } else {
            for (String baseName : this.baseNames) {
                List<String> filenames = calculateAllFilenames(baseName, locale);
                for (String filename : filenames) {
                    PropertiesHolder propHolder =
                            getProperties(filename);
                    String result = propHolder.getProperty(code);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected MessageFormat resolveCode(String code, Locale locale) {
        if (this.cacheMillis < 0) {
            PropertiesHolder propHolder = getMergedProperties(
                    locale);
            MessageFormat result = propHolder.getMessageFormat(code, locale);
            if (result != null) {
                return result;
            }
        } else {
            for (String baseName : this.baseNames) {
                List<String> fileNames = calculateAllFilenames(baseName, locale);
                for (String filename : fileNames) {
                    PropertiesHolder propHolder =
                            getProperties(filename);
                    MessageFormat result = propHolder.getMessageFormat(code, locale);
                    if (result != null) {
                        return result;
                    }
                }
            }
        }
        return null;
    }

    protected PropertiesHolder getMergedProperties(Locale locale) {
        synchronized (this.cachedMergedProperties) {
            PropertiesHolder mergedHolder =
                    this.cachedMergedProperties.get(locale);
            if (mergedHolder != null) {
                return mergedHolder;
            }
            Properties mergedProps = new Properties();
            mergedHolder = new PropertiesHolder(mergedProps, -1);
            for (int i = this.baseNames.length - 1; i >= 0; i--) {
                List filenames = calculateAllFilenames(this.baseNames[i], locale);
                for (int j = filenames.size() - 1; j >= 0; j--) {
                    String filename = (String) filenames.get(j);
                    PropertiesHolder propHolder =
                            getProperties(filename);
                    if (propHolder.getProperties() != null) {
                        mergedProps.putAll(propHolder.getProperties());
                    }
                }
            }
            this.cachedMergedProperties.put(locale, mergedHolder);
            return mergedHolder;
        }
    }

    protected List<String> calculateAllFilenames(String baseName, Locale locale) {
        synchronized (this.cachedFileNames) {
            Map<Locale, List<String>> localeMap = this.cachedFileNames.get(baseName);
            if (localeMap != null) {
                List<String> filenames = localeMap.get(locale);
                if (filenames != null) {
                    return filenames;
                }
            }
            List<String> filenames = new ArrayList<String>(7);
            filenames.addAll(calculateFilenamesForLocale(baseName, locale));
            if (this.fallbackToSystemLocale && !locale.equals(Locale.getDefault())) {
                List<String> fallbackFilenames = calculateFilenamesForLocale(baseName, Locale.
                        getDefault());
                for (String fallbackFilename : fallbackFilenames) {
                    if (!filenames.contains(fallbackFilename)) {
                        // Entry for fallback locale that isn't already in filenames list.
                        filenames.add(fallbackFilename);
                    }
                }
            }
            filenames.add(baseName);
            if (localeMap != null) {
                localeMap.put(locale, filenames);
            } else {
                localeMap = new HashMap<Locale, List<String>>();
                localeMap.put(locale, filenames);
                this.cachedFileNames.put(baseName, localeMap);
            }
            return filenames;
        }
    }

    protected List<String> calculateFilenamesForLocale(String baseName, Locale locale) {
        List<String> result = new ArrayList<String>(3);
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String variant = locale.getVariant();
        StringBuilder temp = new StringBuilder(baseName);

        temp.append('_');
        if (language.length() > 0) {
            temp.append(language);
            result.add(0, temp.toString());
        }

        temp.append('_');
        if (country.length() > 0) {
            temp.append(country);
            result.add(0, temp.toString());
        }

        if (variant.length() > 0 && (language.length() > 0 || country.length() > 0)) {
            temp.append('_').append(variant);
            result.add(0, temp.toString());
        }

        return result;
    }

    protected PropertiesHolder getProperties(String filename) {
        synchronized (this.cachedProperties) {
            PropertiesHolder propHolder =
                    this.cachedProperties.get(filename);
            if (propHolder != null && (propHolder.getRefreshTimestamp() < 0 || propHolder.
                    getRefreshTimestamp() > System.currentTimeMillis() - this.cacheMillis)) {
                // up to date
                return propHolder;
            }
            return refreshProperties(filename, propHolder);
        }
    }

    protected PropertiesHolder refreshProperties(
            String filename, PropertiesHolder propHolder) {
        long refreshTimestamp = (this.cacheMillis < 0) ? -1 : System.currentTimeMillis();

        Resource[] resources = null;
        try {
            if (this.resourceLoader instanceof ResourcePatternResolver) {
                resources = ((ResourcePatternResolver) this.resourceLoader).getResources(filename
                        + PROPERTIES_SUFFIX);
                if (resources == null || resources.length == 0) {
                    resources = ((ResourcePatternResolver) this.resourceLoader).getResources(filename
                            + XML_SUFFIX);
                }
            } else {
                Resource resource = this.resourceLoader.getResource(filename + PROPERTIES_SUFFIX);
                if (!resource.exists()) {
                    resource = this.resourceLoader.getResource(filename + XML_SUFFIX);
                }
                resources = new Resource[1];
                resources[0] = resource;
            }
            if (resources != null && resources.length > 0) {
                propHolder = new PropertiesHolder();
                for (Resource resource : resources) {
                    if (resource.exists()) {
                        long fileTimestamp = -1;
                        if (this.cacheMillis >= 0) {
                            // Last-modified timestamp of file will just be read if caching with timeout.
                            try {
                                fileTimestamp = resource.lastModified();
                                if (propHolder != null && propHolder.getFileTimestamp()
                                        == fileTimestamp) {
                                    if (logger.isDebugEnabled()) {
                                        logger.debug("Re-caching properties for filename ["
                                                + filename
                                                + "] - file hasn't been modified");
                                    }
                                    propHolder.setRefreshTimestamp(refreshTimestamp);
                                    return propHolder;
                                }
                            } catch (IOException ex) {
                                // Probably a class path resource: cache it forever.
                                if (logger.isDebugEnabled()) {
                                    logger.debug(
                                            resource + " could not be resolved in"
                                                    + " the file system - assuming that is hasn't changed", ex);
                                }
                                fileTimestamp = -1;
                            }
                        }
                        try {
                            Properties props = loadProperties(resource, filename);
                            if (propHolder.getProperties() != null) {
                                propHolder.getProperties().putAll(props);
                            } else {
                                propHolder.properties = props;
                            }
                            propHolder.fileTimestamp = fileTimestamp;
                        } catch (IOException ex) {
                            if (logger.isWarnEnabled()) {
                                logger.warn("Could not parse properties file [" + resource.
                                        getFilename()
                                        + "]", ex);
                            }
                        }
                    } else {
                        // Resource does not exist.
                        if (logger.isDebugEnabled()) {
                            logger.debug("No properties file found for [" + resource.getFilename()
                                    + "] - neither plain properties nor XML");
                        }
                    }
                }
            } else {
                // Resource does not exist.
                if (logger.isDebugEnabled()) {
                    logger.debug("No properties files found for [" + filename
                            + "] - neither plain properties nor XML");
                }
                // Empty holder representing "not found".
                propHolder = new PropertiesHolder();
            }
        } catch (IOException iOException) {
            if (logger.isWarnEnabled()) {
                logger.warn("Could not match pattern [" + filename
                        + "]", iOException);
            }
            // Empty holder representing "not valid".
            propHolder = new PropertiesHolder();
        }

        propHolder.setRefreshTimestamp(refreshTimestamp);
        this.cachedProperties.put(filename, propHolder);
        return propHolder;
    }

    protected Properties loadProperties(Resource resource, String filename) throws IOException {
        InputStream is = resource.getInputStream();
        Properties props = new Properties();
        try {
            if (resource.getFilename().endsWith(XML_SUFFIX)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Loading properties [" + resource.getFilename() + "]");
                }
                this.propertiesPersister.loadFromXml(props, is);
            } else {
                String encoding = null;
                if (this.fileEncodings != null) {
                    encoding = this.fileEncodings.getProperty(filename);
                }
                if (encoding == null) {
                    encoding = this.defaultEncoding;
                }
                if (encoding != null) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Loading properties [" + resource.getFilename()
                                + "] with encoding '" + encoding + "'");
                    }
                    this.propertiesPersister.load(props, new InputStreamReader(is, encoding));
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Loading properties [" + resource.getFilename() + "]");
                    }
                    this.propertiesPersister.load(props, is);
                }
            }
            return props;
        } finally {
            is.close();
        }
    }

    public void clearCache() {
        logger.debug("Clearing entire resource bundle cache");
        synchronized (this.cachedProperties) {
            this.cachedProperties.clear();
        }
        synchronized (this.cachedMergedProperties) {
            this.cachedMergedProperties.clear();
        }
    }

    public void clearCacheIncludingAncestors() {
        clearCache();
        if (getParentMessageSource() instanceof ReloadableResourceBundleMessageSource) {
            ((ReloadableResourceBundleMessageSource) getParentMessageSource()).
                    clearCacheIncludingAncestors();
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + ": baseNames=["
                + StringUtils.arrayToCommaDelimitedString(this.baseNames) + "]";
    }

    protected class PropertiesHolder {

        private Properties properties;
        private long fileTimestamp = -1;
        private long refreshTimestamp = -1;
        private final Map<String, Map<Locale, MessageFormat>> cachedMessageFormats =
                new HashMap<String, Map<Locale, MessageFormat>>();

        public PropertiesHolder(Properties properties, long fileTimestamp) {
            this.properties = properties;
            this.fileTimestamp = fileTimestamp;
        }

        public PropertiesHolder() {
        }

        public Properties getProperties() {
            return properties;
        }

        public long getFileTimestamp() {
            return fileTimestamp;
        }

        public void setRefreshTimestamp(long refreshTimestamp) {
            this.refreshTimestamp = refreshTimestamp;
        }

        public long getRefreshTimestamp() {
            return refreshTimestamp;
        }

        public String getProperty(String code) {
            if (this.properties == null) {
                return null;
            }
            return this.properties.getProperty(code);
        }

        public MessageFormat getMessageFormat(String code, Locale locale) {
            if (this.properties == null) {
                return null;
            }
            synchronized (this.cachedMessageFormats) {
                Map<Locale, MessageFormat> localeMap = this.cachedMessageFormats.get(code);
                if (localeMap != null) {
                    MessageFormat result = localeMap.get(locale);
                    if (result != null) {
                        return result;
                    }
                }
                String msg = this.properties.getProperty(code);
                if (msg != null) {
                    if (localeMap == null) {
                        localeMap = new HashMap<Locale, MessageFormat>();
                        this.cachedMessageFormats.put(code, localeMap);
                    }
                    MessageFormat result = createMessageFormat(msg, locale);
                    localeMap.put(locale, result);
                    return result;
                }
                return null;
            }
        }
    }
}
