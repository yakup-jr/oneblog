package com.oneblog.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Dev config.
 */
@Configuration
@Profile("dev")
@Import({OneblogConfig.class})
@PropertySource("classpath:/application-dev.yml")
@Slf4j
public class DevConfig {

}
