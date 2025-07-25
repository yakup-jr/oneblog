package net.oneblog.sharedconfig.prod;

import net.oneblog.sharedconfig.standart.OneblogConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * The type Prod config.
 */
@Configuration
@Profile("prod")
@Import({OneblogConfig.class})
public class ProdConfig {
}
