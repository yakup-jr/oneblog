package com.oneblog.config;

import com.zaxxer.hikari.HikariDataSource;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@TestConfiguration
@Import({OneblogConfig.class})
@Profile("test")
public class TestConfig {

	@Bean
	@Primary
	public PostgreSQLContainer<?> postgresContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
		var container = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3-alpine"));
		dynamicPropertyRegistry.add("postgresql.driver", container::getDriverClassName);
		return container;
	}

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


	@Bean
	public PostgreSQLContainer<?> postgreSQLContainer() {
		return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3-alpine"));
	}

	@Bean
	public DataSource dataSource(PostgreSQLContainer<?> postgreSQLContainer) {
		var hikariDataSource = new HikariDataSource();
		hikariDataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
		hikariDataSource.setUsername(postgreSQLContainer.getUsername());
		hikariDataSource.setPassword(postgreSQLContainer.getPassword());
		return hikariDataSource;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource(postgreSQLContainer()));
	}

}
