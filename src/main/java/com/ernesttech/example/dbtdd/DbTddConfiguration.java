package com.ernesttech.example.dbtdd;

import com.jolbox.bonecp.BoneCPDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DbTddConfiguration {

    @Autowired
    private Environment env;

    private static final String PROPERTY_NAME_DB_DRIVER = "db.driver";
    private static final String PROPERTY_NAME_DB_PASSWORD = "db.password";
    private static final String PROPERTY_NAME_DB_URL = "db.url";
    private static final String PROPERTY_NAME_DB_USERNAME = "db.username";

    @Bean(destroyMethod = "close", name = "dataSource")
    @Primary
    public DataSource dataSource() {
        BoneCPDataSource dataSource = new BoneCPDataSource();

        dataSource.setDriverClass(env.getRequiredProperty(PROPERTY_NAME_DB_DRIVER));
        dataSource.setJdbcUrl(env.getRequiredProperty(PROPERTY_NAME_DB_URL));
        dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DB_USERNAME));
        dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DB_PASSWORD));

        return dataSource;
    }

    @Bean(name = "defConfiguration")
    public DefaultConfiguration configuration() {
        DefaultConfiguration jooqConfiguration = new DefaultConfiguration();

        jooqConfiguration.set(connectionProvider());
        jooqConfiguration.set(SQLDialect.POSTGRES);

        return jooqConfiguration;
    }

    @Bean(name = "lazyConnDataSource")
    public LazyConnectionDataSourceProxy lazyConnectionDataSource() {
        return new LazyConnectionDataSourceProxy(dataSource());
    }

    @Bean(name = "transAwareDataSource")
    public TransactionAwareDataSourceProxy transactionAwareDataSource() {
        return new TransactionAwareDataSourceProxy(lazyConnectionDataSource());
    }

    @Bean(name = "transManager")
    public DataSourceTransactionManager transactionManager() {
        return new DataSourceTransactionManager(lazyConnectionDataSource());
    }

    @Bean(name = "connectionProvider")
    public DataSourceConnectionProvider connectionProvider() {
        return new DataSourceConnectionProvider(transactionAwareDataSource());
    }

    @Bean(name = "defDSLContext")
    public DefaultDSLContext dsl() {
        return new DefaultDSLContext(configuration());
    }


}
