package com.tollwood.dao;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatasourceConfig {
    @Bean(name = "postgresDataSource")
    public DataSource dataSource() throws URISyntaxException {
        String dataBaseUrl = System.getenv("DATABASE_URL");
        if (dataBaseUrl == null) {
            throw new MissingDatabaseUrlException();
        }
        URI dbUri = new URI(dataBaseUrl);

        String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();

        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(username);
        basicDataSource.setPassword(password);
        basicDataSource.setConnectionProperties("sslmode=require");

        return basicDataSource;
    }
}
