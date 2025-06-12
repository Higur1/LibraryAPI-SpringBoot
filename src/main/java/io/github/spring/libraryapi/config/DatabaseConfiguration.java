package io.github.spring.libraryapi.config;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {
    @Value("${spring.datasource.url}")
    String url;
    @Value("${spring.datasource.username}")
    String username;
    @Value("${spring.datasource.password}")
    String password;
    @Value("${spring.datasource.driver-class-name}")
    String driver;

    //basic datasource - not recommend
    /*
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setDriverClassName(driver);
        return dataSource;
    }*/

    /**
     * configuration Hikary
     * https://github.com/brettwooldridge/HikariCP
     *
     * @return
     */
    @Bean
    public DataSource hikariDataSource() {
        HikariConfig config = new HikariConfig();
        config.setUsername(username);
        config.setJdbcUrl(url);
        config.setPassword(password);
        config.setDriverClassName(driver);

        //max users
        config.setMaximumPoolSize(10);

        //first connection
        config.setMinimumIdle(1);

        config.setPoolName("library-db-pool");

        //set time to connection
        config.setMaxLifetime(600000); //10m

        config.setConnectionTimeout(100000); //1m

        //test query
        config.setConnectionTestQuery("select 1");

        return new HikariDataSource(config);
    }
}
