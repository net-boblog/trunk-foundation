package com.tumbleweed.platform.trunk.util.manifest;

import org.springframework.util.StringUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

public abstract class ManifestUtil {

    public static Properties read(
            String groupId, String artifactId, ServletContext servletContext)
            throws IOException {
        Properties properties;

        if (StringUtils.isEmpty(groupId) || StringUtils.isEmpty(artifactId)) {
            properties = readFromServletPath(servletContext);
        } else {
            properties = readFromClassPath(groupId, artifactId);
        }

        return properties;
    }

    public static Properties readFromClassPath(String groupId, String artifactId) throws IOException {
        Properties properties = new Properties();

        try {
            Enumeration<URL> resources = Thread.currentThread()
                    .getContextClassLoader()
                    .getResources("META-INF/MANIFEST.MF");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                if (url.getPath().contains(artifactId)) {
                    InputStream stream = url.openStream();

                    properties = readPropertiesFromManifest(stream);

                    if (properties.containsValue(groupId)) {
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new IOException(ex.getMessage(), ex);
        }

        return properties;
    }

    public static Properties readFromServletPath(ServletContext servletContext) throws IOException {
        Properties properties;

        try {
            InputStream stream = servletContext.getResourceAsStream("/META-INF/MANIFEST.MF");
            properties = readPropertiesFromManifest(stream);
        } catch (Exception ex) {
            throw new IOException(ex.getMessage(), ex);
        }

        return properties;
    }

    private static Properties readPropertiesFromManifest(InputStream stream) throws IOException {
        Properties properties = new Properties();

        if (stream != null) {
            try {
                Manifest manifest = new Manifest(stream);

                final Attributes attrs = manifest.getMainAttributes();

                properties = new Properties();

                for (final Object key : attrs.keySet()) {
                    final String value = attrs.getValue(Attributes.Name.class.cast(key));
                    properties.put(key.toString(), value);
                }
            } finally {
                stream.close();
            }
        }

        return properties;
    }

}
