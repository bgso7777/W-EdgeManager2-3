package com.inswave.appplatform.config;

import com.inswave.appplatform.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

@Slf4j
@Configuration
@EnableJpaRepositories(
basePackages = { "com.inswave.appplatform" },
excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.inswave.appplatform.external.*"),
entityManagerFactoryRef = "wemEntityManager",
transactionManagerRef = "wemTransactionManager"
)
public class WemDataSourceConfiguration {
    @Autowired
    private Environment env;

    @Bean("jpaWemProperties")
    @Primary
    public JpaProperties jpaWemProperties() {
        return new JpaProperties();
    }

    @Value("${spring.datasource.wedgemanager.jndi-name:#{null}}")
    private String wemJndiName;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.wedgemanager")
    @Primary
    public DataSource wemDataSource() {
        if (wemJndiName != null) {
            JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
            DataSource dataSource = dataSourceLookup.getDataSource(wemJndiName);
            return dataSource;
        }

        return DataSourceBuilder.create().build();
    }

    // EntityManager bean
    @Bean(name = "wemEntityManager")
    @Primary
    public LocalContainerEntityManagerFactoryBean wemEntityManager() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(wemDataSource());
        em.setPackagesToScan(
        "com.inswave.appplatform.log",
        "com.inswave.appplatform.log.dao",
        "com.inswave.appplatform.dao",
        "com.inswave.appplatform.domain",
        "com.inswave.appplatform.service.domain",
        "com.inswave.appplatform.wedgemanager",
        "com.inswave.appplatform.deployer.dao",
        "com.inswave.appplatform.deployer.domain"
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaPropertyMap(jpaWemProperties().getProperties());

        Connection connection = null;
        String productName = "";
        try {
            connection = wemDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();
            productName = metaData.getDatabaseProductName().toUpperCase();
            Config.getInstance().setWemDatabaseVendor(productName);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        log.info("Database product name: {}", productName);

        return em;
    }

    // TransactionManager bean
    @Bean(name = "wemTransactionManager")
    @Primary
    public PlatformTransactionManager wemTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(wemEntityManager().getObject());
        return transactionManager;
    }
}