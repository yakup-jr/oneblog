package com.oneblog;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


@SpringBootApplication
public class OneblogApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder().sources(OneblogApplication.class).run(args);
	}
}
