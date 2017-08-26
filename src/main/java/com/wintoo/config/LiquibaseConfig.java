package com.wintoo.config;


import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig  {

    @Bean
    public SpringLiquibase liquibase(@Qualifier("primaryDataSource")DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:liquibase/db.changelog.master.yaml");
        liquibase.setShouldRun(false);
        return liquibase;
    }

}
