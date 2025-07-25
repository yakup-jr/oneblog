package net.oneblog.auth.config;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class GoogleConfig {

	@Value("${spring.security.oauth2.client.registration.google.client-id}")
	private String clientId;

	/**
	 * Google id token verifier google id token verifier.
	 *
	 * @return the google id token verifier
	 */
	@Bean
	public GoogleIdTokenVerifier googleIdTokenVerifier() {
		return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
			       .setAudience(Collections.singletonList(clientId))
			       .build();
	}

}
