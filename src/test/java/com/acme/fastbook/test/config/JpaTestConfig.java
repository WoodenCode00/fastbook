package com.acme.fastbook.test.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuration class to setup persistence layer for integration testing
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.acme.fastbook.persistence.repository")
@EnableTransactionManagement
@Sql({"/insert-booking-item.sql"}) // inserts data to DB
public class JpaTestConfig {

	/** Spring environment */
    @Autowired
    private Environment env;
    
    /**
     * Configures {@link DataSource} bean
     * 
     * @return {@link DataSource} bean
     */
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
        dataSource.setUrl(env.getProperty("spring.datasource.url"));
        dataSource.setUsername(env.getProperty("spring.datasource.user"));
        dataSource.setPassword(env.getProperty("spring.datasource.pass"));

        return dataSource;
    }
}
