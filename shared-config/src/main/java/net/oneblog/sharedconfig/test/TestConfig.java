package net.oneblog.sharedconfig.test;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import net.oneblog.sharedconfig.standart.OneblogConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


/**
 * The type Test config.
 */
@TestConfiguration
@Import({OneblogConfig.class})
@Profile("test")
public class TestConfig {

    @Value("${spring.mail.username}")
    private String mailUsername;
    @Value("${spring.mail.password}")
    private String mailPassword;

    /**
     * Postgres container postgre sql container.
     *
     * @param dynamicPropertyRegistry the dynamic property registry
     * @return the postgre sql container
     */
    @Bean
    @Primary
    public PostgreSQLContainer<?> postgresContainer(
        DynamicPropertyRegistry dynamicPropertyRegistry) {
        var container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3-alpine"));
        container.withExposedPorts(5432);
        dynamicPropertyRegistry.add("postgresql.driver", container::getDriverClassName);
        return container;
    }

    /**
     * Green mail container generic container.
     *
     * @param dynamicPropertyRegistry the dynamic property registry
     * @return the generic container
     */
    @Bean
    public GenericContainer greenMailContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        GenericContainer<?> container =
            new GenericContainer<>(DockerImageName.parse("greenmail/standalone:1.6.1"));
        container.waitingFor(Wait.forLogMessage(".*Starting GreenMail standalone.*", 1))
            .withEnv("GREENMAIL_OPTS",
                "-Dgreenmail.setup.test.smtp -Dgreenmail.hostname=0.0.0.0 -Dgreenmail" +
                    ".users=" + mailUsername + ":" + mailPassword)
            .withExposedPorts(3025);
        dynamicPropertyRegistry.add("spring.mail.host", container::getHost);
        dynamicPropertyRegistry.add("spring.mail.port", container::getFirstMappedPort);

        return container;
    }

    /**
     * Spring liquibase spring liquibase.
     *
     * @param dataSource the data source
     * @return the spring liquibase
     */
    @Bean
    public SpringLiquibase springLiquibase(DataSource dataSource) {

        int attemptCount = 0;
        int maxAttempts = 20;
        Connection connection = null;

        while (attemptCount < maxAttempts) {
            try {
                connection = dataSource.getConnection();
                if (connection != null) {
                    break;
                }
            } catch (SQLException e) {
                attemptCount++;
            }
        }

        var liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setChangeLog("classpath:db/changelog/db.changelog-master.yaml");
        return liquibase;
    }


    /**
     * Postgre sql container postgre sql container.
     *
     * @return the postgre sql container
     */
    @Bean
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3-alpine"));
    }

    /**
     * Data source data source.
     *
     * @param postgreSQLContainer the postgre sql container
     * @return the data source
     */
    @Bean
    public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
        var hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
        hikariDataSource.setUsername(postgreSQLContainer.getUsername());
        hikariDataSource.setPassword(postgreSQLContainer.getPassword());
        return hikariDataSource;
    }

    /**
     * Transaction manager platform transaction manager.
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource(postgreSQLContainer()));
    }

}
