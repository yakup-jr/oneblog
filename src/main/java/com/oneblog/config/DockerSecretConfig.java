package com.oneblog.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Configuration
@Profile({"dev", "prod"})
public class DockerSecretConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(DockerSecretConfig.class);

	@Value("${DATASOURCE_URL}")
	private String datasourceUrl;

	@Bean
	public DataSource dataSource() throws IOException {
		String username = Files.readString(Path.of("/run/secrets/db_username")).trim();
		String password = Files.readString(Path.of("/run/secrets/db_password")).trim();

		LOGGER.info("Loaded secrets for database user: {}", maskValue(username));


		return DataSourceBuilder.create()
		                        .url(datasourceUrl)
		                        .username(username)
		                        .password(password)
		                        .driverClassName("org.postgresql.Driver")
		                        .build();
	}

	@PostConstruct
	public void loadSecrets() throws IOException {
		String googleClientId = Files.readString(Path.of("/run/secrets/oauth_google_client_id")).trim();
		String googleClientSecret = Files.readString(Path.of("/run/secrets/oauth_google_client_secret")).trim();
		String jwtSecretKey = Files.readString(Path.of("/run/secrets/jwt_secret_key")).trim();
		String mailUsername = Files.readString(Path.of("/run/secrets/mail_username")).trim();
		String mailPassword = Files.readString(Path.of("/run/secrets/mail_password")).trim();

		System.setProperty("spring.security.oauth2.client.registration.google.client-id", googleClientId);
		System.setProperty("spring.security.oauth2.client.registration.google.client-secret", googleClientSecret);
		System.setProperty("security.jwt.secret_key", jwtSecretKey);
		System.setProperty("spring.mail.username", mailUsername);
		System.setProperty("spring.mail.password", mailPassword);

	}

	private String maskValue(String value) {
		if (value == null || value.isEmpty()) {
			return "N/A";
		}
		return value.charAt(0) + "****" + value.charAt(value.length() - 1);
	}

}
