package com.tumbleweed.platform.trunk.data.core.service.internal;

import com.tumbleweed.platform.trunk.data.core.service.SoupeDataManager;
import org.hibernate.ejb.Ejb3Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class DefaultSoupeDataManager implements SoupeDataManager {

    @Autowired
    private LocalContainerEntityManagerFactoryBean entityManagerFactory;

    public void exportSchema(
            String outputFileAbsolutePath, String delimiter,
            boolean toConsole, boolean toDatabase, boolean justDrop,
            boolean justCreate) {
        Ejb3Configuration configuration = new Ejb3Configuration()
                .configure(entityManagerFactory.getPersistenceUnitInfo(),
                        new Properties());
        SchemaExport schemaExport = new SchemaExport(
                configuration.getHibernateConfiguration());
        schemaExport.setOutputFile(outputFileAbsolutePath);
        schemaExport.setFormat(true);
        if (delimiter == null) {
            delimiter = ";";
        }
        schemaExport.setDelimiter(delimiter);
        schemaExport.execute(toConsole, toDatabase, justDrop, justCreate);
    }
}
