package com.oneblog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * The type Oneblog application.
 */
@SpringBootApplication
public class OneblogApplication {

	/**
	 * The entry point of application.
	 *
	 * @param args the input arguments
	 */
	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(OneblogApplication.class).run(args);
	}
}
