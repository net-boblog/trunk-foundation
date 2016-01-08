package com.tumbleweed.platform.trunk.data.core.component;

import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.persistence.EntityManagerFactory;
import javax.persistence.spi.PersistenceUnitInfo;

public class SoupeLocalContainerEntityManagerFactoryBean extends LocalContainerEntityManagerFactoryBean {

    @Override
    protected void postProcessEntityManagerFactory(EntityManagerFactory emf, PersistenceUnitInfo pui) {

    }

}
