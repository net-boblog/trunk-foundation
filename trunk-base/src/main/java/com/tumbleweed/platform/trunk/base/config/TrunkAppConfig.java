package com.tumbleweed.platform.trunk.base.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableScheduling
@ComponentScan(
        basePackages = {
                "com.tumbleweed.platform.trunk.core.config",
                "com.tumbleweed.platform.trunk.*.core.config",
                "com.tumbleweed.product.*.base.config",
                "com.tumbleweed.platform.trunk.core.component",
                "com.tumbleweed.platform.trunk.core.service",
                "com.tumbleweed.platform.trunk.core.event",
                "com.tumbleweed.platform.trunk.*.core.component",
                "com.tumbleweed.platform.trunk.*.core.service",
                "com.tumbleweed.platform.trunk.*.core.event",
                "com.tumbleweed.platform.trunk.web.application",
                "com.tumbleweed.platform.trunk.*.web.application",
                "com.tumbleweed.platform.trunk.web.component",
                "com.tumbleweed.platform.trunk.*.web.component",
                "com.tumbleweed.platform.trunk.web.widget",
                "com.tumbleweed.platform.trunk.*.web.widget",
                "com.tumbleweed.product.*.data.config",
                "com.tumbleweed.platform.trunk.core.repository.jdbc",
                "com.tumbleweed.platform.trunk.core.repository.cache",
                "com.tumbleweed.platform.trunk.*.core.repository.jdbc",
                "com.tumbleweed.platform.trunk.*.core.repository.cache"
        }
)
@EnableTransactionManagement
public class TrunkAppConfig implements TrunkConfig {

    private static Log logger = LogFactory.getLog(TrunkAppConfig.class);

    @Value("${trunk.messages.refresh.interval}")
    private int extensionalMessagesRefreshInterval;

    @Bean
    public static BeanFactoryPostProcessor getBeanFactoryPostProcessor() throws IOException {
        PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
        configurer.setSystemPropertiesMode(PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_OVERRIDE);
        configurer.setIgnoreResourceNotFound(true);

        PathMatchingResourcePatternResolver pathMatchingResourcePatternResolver
                = new PathMatchingResourcePatternResolver();

        Resource[] resources = pathMatchingResourcePatternResolver.getResources("classpath*:*-context.properties");

        List<Resource> allOfResources = new LinkedList<Resource>();
        allOfResources.addAll(Arrays.asList(resources));

        try {
            String userHome = System.getProperty("user.home");

            logger.info("User Home: " + userHome + ". You can place customized configuration here.");

            for (Resource resource : resources) {
                FileSystemResource fileSystemResource
                        = new FileSystemResource(userHome + "/.trunk/" + resource.getFilename());

                if (fileSystemResource.exists()) {
                    allOfResources.add(fileSystemResource);
                }
            }
        } catch (Exception ex) {
            logger.error("Load configuration from home directory of user failed.");
        }

        configurer.setLocations(allOfResources.toArray(new Resource[allOfResources.size()]));

        return configurer;
    }

    @Bean(name = "messages")
    public MessageSource getTrunkMessages() {
        ReloadableResourceBundleMessageSource trunkMessages = new ReloadableResourceBundleMessageSource();
        trunkMessages.setBaseName("classpath*:i18n/messages");
        trunkMessages.setDefaultEncoding("UTF-8");
        trunkMessages.setUseCodeAsDefaultMessage(true);
        return trunkMessages;
    }

    @Bean(name = "messages-ext")
    public MessageSource getTrunkExtensionalMessages() {
        ReloadableResourceBundleMessageSource soupeExtensionalMessages = new ReloadableResourceBundleMessageSource();
        soupeExtensionalMessages.setBaseName("/WEB-INF/i18n/messages");
        soupeExtensionalMessages.setCacheSeconds(extensionalMessagesRefreshInterval);
        soupeExtensionalMessages.setDefaultEncoding("UTF-8");

        return soupeExtensionalMessages;
    }

}
