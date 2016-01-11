package com.tumbleweed.platform.trunk.base.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

public final class TrunkConstants {

    private static Log logger = LogFactory.getLog(TrunkConstants.class);

    public static final String TRUNK_DATA_INITIALIZER_PAGE_ENABLED_KEY = "trunk.data.initializer.page.enabled";

    public static final boolean TRUNK_DATA_INITIALIZER_PAGE_ENABLED;

    public static final String TRUNK_DATA_INITIALIZER_RESOURCE_EXT_SCHEMA_ENABLED_KEY
            = "trunk.data.initializer.resource.extension.schema.enabled";

    public static final boolean TRUNK_DATA_INITIALIZER_RESOURCE_EXT_SCHEMA_ENABLED;

    public static final String TRUNK_SECURITY_DOMAIN_ENABLED_KEY = "trunk.security.domain.enabled";

    public static final boolean TRUNK_SECURITY_DOMAIN_ENABLED;

    public static final String TRUNK_UI_COMPATIBILITY_VIEW_REQUIRED_KEY = "trunk.ui.compatibility.view.required";

    public static final boolean TRUNK_UI_COMPATIBILITY_VIEW_REQUIRED;

    public static final String TRUNK_UI_THEME_DEFAULT_KEY = "trunk.ui.theme.default";

    public static final String TRUNK_UI_THEME_PARAMETER_NAME = "theme";

    public static final String TRUNK_UI_THEME_DEFAULT;

    public static final String TRUNK_UI_LAYOUT_DEFAULT_KEY = "trunk.ui.layout.default";

    public static final String TRUNK_UI_LAYOUT_PARAMETER_NAME = "layout";

    public static final String TRUNK_UI_LAYOUT_DEFAULT;

    public static final String TRUNK_UI_WIDGETSET_DEFAULT_KEY = "trunk.ui.widgetset.default";

    public static final String TRUNK_UI_WIDGETSET_PARAMETER_NAME = "widgetset";

    public static final String TRUNK_UI_WIDGETSET_DEFAULT;

    static {
        Properties soupeContextProperties = new Properties();
        load(soupeContextProperties);

        TRUNK_DATA_INITIALIZER_PAGE_ENABLED =
                Boolean.parseBoolean(soupeContextProperties.getProperty(TRUNK_DATA_INITIALIZER_PAGE_ENABLED_KEY));

        TRUNK_DATA_INITIALIZER_RESOURCE_EXT_SCHEMA_ENABLED =
                Boolean.parseBoolean(
                        soupeContextProperties.getProperty(TRUNK_DATA_INITIALIZER_RESOURCE_EXT_SCHEMA_ENABLED_KEY));

        TRUNK_SECURITY_DOMAIN_ENABLED =
                Boolean.parseBoolean(soupeContextProperties.getProperty(TRUNK_SECURITY_DOMAIN_ENABLED_KEY));

        TRUNK_UI_COMPATIBILITY_VIEW_REQUIRED =
                Boolean.parseBoolean(soupeContextProperties.getProperty(TRUNK_UI_COMPATIBILITY_VIEW_REQUIRED_KEY));

        TRUNK_UI_THEME_DEFAULT = soupeContextProperties.getProperty(TRUNK_UI_THEME_DEFAULT_KEY);
        TRUNK_UI_LAYOUT_DEFAULT = soupeContextProperties.getProperty(TRUNK_UI_LAYOUT_DEFAULT_KEY);
        TRUNK_UI_WIDGETSET_DEFAULT = soupeContextProperties.getProperty(TRUNK_UI_WIDGETSET_DEFAULT_KEY);
    }

    private static void load(Properties properties) {
        try {
            PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver
                    = new PathMatchingResourcePatternResolver();

            Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:*-context.properties");

            List<Resource> allOfResources = new LinkedList<Resource>();
            allOfResources.addAll(Arrays.asList(resources));

            try {
                String userHome = System.getProperty("user.home");

                for (Resource resource : resources) {
                    FileSystemResource fileSystemResource
                            = new FileSystemResource(userHome + "/.trunk/" + resource.getFilename());

                    if (fileSystemResource.exists()) {
                        allOfResources.add(fileSystemResource);
                    }
                }
            } catch (Exception ex) {
                logger.warn(ex.getMessage(), ex);
            }

            for (Resource resource : allOfResources) {
                logger.debug("Loading configuration from file: " + resource.getFilename());
                properties.load(resource.getInputStream());
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read context properties, " + e.getMessage(), e);
        }
    }

}
