package com.tumbleweed.platform.trunk.data.core.config;

import com.tumbleweed.platform.trunk.base.config.TrunkConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaDialect;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Configuration
@ImportResource({"classpath:/script/soupe-data-context.xml"})
@EnableJpaRepositories(
        basePackages = {
                "com.tumbleweed.platform.trunk.core.repository.jpa",
                "com.tumbleweed.platform.trunk.*.core.repository.jpa"
        }
)
public class TrunkDataConfig implements TrunkConfig {

    @Value("${trunk.persistence.type}")
    private String type;
    @Value("${trunk.persistence.dialect}")
    private String dialect;
    @Value("${trunk.persistence.username}")
    private String defaultSchema;
    @Value("${trunk.persistence.showsql}")
    private boolean showsql;
    @Value("${trunk.persistence.generateDdl}")
    private boolean generateDdl;
    @Value("${trunk.persistence.entity.packages}")
    private String entityPackages;
    @Value("${trunk.persistence.cache.enabled}")
    private boolean cacheEnabled;

    @Bean(name = "entityManagerFactory")
    @Autowired
    public LocalContainerEntityManagerFactoryBean getEntityManagerFactory(
            DataSource dataSource, JpaDialect jpaDialect) {
        LocalContainerEntityManagerFactoryBean
                localContainerEntityManagerFactoryBean
                = new LocalContainerEntityManagerFactoryBean();

        localContainerEntityManagerFactoryBean.setPersistenceUnitName("TRUNK");

        localContainerEntityManagerFactoryBean
                .setDataSource(dataSource);

        HibernateJpaVendorAdapter jpaVendorAdapter
                = new HibernateJpaVendorAdapter();
        jpaVendorAdapter.setDatabase(Database.valueOf(type));
        jpaVendorAdapter.setShowSql(showsql);
        jpaVendorAdapter.setGenerateDdl(generateDdl);

        jpaVendorAdapter.setDatabasePlatform(dialect);
        localContainerEntityManagerFactoryBean
                .setJpaVendorAdapter(jpaVendorAdapter);
        localContainerEntityManagerFactoryBean
                .setJpaDialect(jpaDialect);

        Properties hibernateProperties = new Properties();

        hibernateProperties.put("hibernate.default_schema", defaultSchema);
        hibernateProperties.put("hibernate.cache.use_query_cache", cacheEnabled);
        hibernateProperties.put("hibernate.cache.use_second_level_cache", cacheEnabled);
        hibernateProperties.put(
                "hibernate.cache.region.factory_class",
                "org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory");
        hibernateProperties.put("hibernate.max_fetch_depth", 1);
        hibernateProperties.put("hibernate.default_batch_fetch_size", 8);
        hibernateProperties.put("hibernate.default_entity_mode", "pojo");
        hibernateProperties.put("hibernate.format_sql", false);
        hibernateProperties.put("hibernate.order_updates", true);
        hibernateProperties.put("hibernate.generate_statistics", true);
        hibernateProperties.put("hibernate.jdbc.batch_versioned_data", true);
        hibernateProperties.put("hibernate.jdbc.use_get_generated_keys", true);
        hibernateProperties.put("hibernate.flush_before_completion", true);
        hibernateProperties.put("hibernate.query.jpaql_strict_compliance", true);
        hibernateProperties.put("hibernate.jdbc.batch_size", 200);
        hibernateProperties.put("hibernate.search.lucene_version", "LUCENE_36");

        localContainerEntityManagerFactoryBean.setJpaProperties(hibernateProperties);

        localContainerEntityManagerFactoryBean.setMappingResources(
                "META-INF/JBPMorm.xml", "META-INF/TaskAuditorm.xml", "META-INF/Taskorm.xml");

        List<String> packages = new ArrayList<String>();
        packages.addAll(Arrays.asList(entityPackages.trim().split(",")));
        packages.add("org.drools");
        packages.add("org.jbpm");

        localContainerEntityManagerFactoryBean
                .setPackagesToScan(packages.toArray(new String[packages.size()]));

        return localContainerEntityManagerFactoryBean;
    }

    @Bean(name = "transactionManager")
    @Autowired
    public PlatformTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }

    @Bean(name = "jpaDialect")
    public JpaDialect getJpaDialect() {
        return new HibernateJpaDialect();
    }

    @Bean(name = "jdbcTemplate")
    @Autowired
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        jdbcTemplate.setDataSource(dataSource);

        return jdbcTemplate;
    }

}
