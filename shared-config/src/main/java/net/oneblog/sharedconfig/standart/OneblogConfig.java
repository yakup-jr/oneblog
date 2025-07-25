package net.oneblog.sharedconfig.standart;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

/**
 * The type Oneblog config.
 */
@Configuration
public class OneblogConfig {
	/**
	 * Random random.
	 *
	 * @return the random
	 */
	@Bean
	public Random random() {
		return new Random();
	}
}
