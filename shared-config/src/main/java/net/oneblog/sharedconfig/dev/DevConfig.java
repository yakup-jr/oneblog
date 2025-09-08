package net.oneblog.sharedconfig.dev;

import net.oneblog.sharedconfig.standart.OneblogConfig;
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
public class DevConfig {

}
