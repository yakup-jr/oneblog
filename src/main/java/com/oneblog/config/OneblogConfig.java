package com.oneblog.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Collections;
import java.util.Random;

@Import(SecurityConfig.class)
@Configuration
public class OneblogConfig {

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;

	@Bean
	public Random random() {
		return new Random();
	}

	@Bean
	public GoogleIdTokenVerifier googleIdTokenVerifier() {
		return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
			       .setAudience(Collections.singletonList(clientId))
			       .build();
	}
}
