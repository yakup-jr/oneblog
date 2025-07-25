package net.oneblog.sharedconfig.test;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;


@TestConfiguration
@Profile("test")
public class TestConfig {

    @Bean
    @ServiceConnection // This is the key improvement!
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.3-alpine"));
    }

    @Bean
    public GenericContainer<?> greenMailContainer(DynamicPropertyRegistry dynamicPropertyRegistry) {
        GenericContainer<?> container = new GenericContainer<>(
            DockerImageName.parse("greenmail/standalone:2.0.1"))
            .withExposedPorts(3025, 3110)
            .withEnv("GREENMAIL_OPTS", "-Dgreenmail.setup.test.all -Dgreenmail.hostname=0.0.0.0");

        dynamicPropertyRegistry.add("spring.mail.host", container::getHost);
        dynamicPropertyRegistry.add("spring.mail.port",
            () -> container.getMappedPort(3025).toString());

        return container;
    }

}
