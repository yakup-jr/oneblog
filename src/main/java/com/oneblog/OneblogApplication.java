package com.oneblog;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;


@Log4j2
@SpringBootApplication
public class OneblogApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder().profiles("dev").sources(OneblogApplication.class).run(args);
	}

	@Bean
	ApplicationRunner applicationRunner(Environment env) {
		return args -> {
			log.info(env.getProperty("message-from-application-properties"));
		};
	}


}
