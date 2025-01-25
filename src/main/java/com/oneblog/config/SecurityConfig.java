package com.oneblog.config;

import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.oneblog.cookie.*;
import com.oneblog.user.User;
import com.oneblog.user.UserRepository;
import com.oneblog.user.role.RoleRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http, TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer,
		TokenCookieJweStringSerializer tokenCookieJweStringSerializer) throws Exception {
		TokenCookieSessionAuthenticationStrategy tokenCookieSessionAuthenticationStrategy =
			new TokenCookieSessionAuthenticationStrategy();
		tokenCookieSessionAuthenticationStrategy.setTokenStringSerializer(tokenCookieJweStringSerializer);

		http.httpBasic(Customizer.withDefaults()).addFilterAfter(new GetCsrfToken(), ExceptionTranslationFilter.class)
		    .authorizeHttpRequests(
			    authorizeHttpRequests -> authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/api/v1/user",
			                                                                   "/api/v1/article/",
			                                                                   "/api/v1/articles/label")
			                                                  .hasRole("ADMIN")
			                                                  .requestMatchers(HttpMethod.GET, "/api/v1/users")
			                                                  .hasRole("ADMIN")
			                                                  .requestMatchers(HttpMethod.DELETE,
			                                                                   "/api/v1/user/{userId}",
			                                                                   "/api/v1/article/{articleId}",
			                                                                   "/api/v1/articles/label/{labelId}")
			                                                  .hasRole("ADMIN")
			                                                  .requestMatchers(HttpMethod.GET, "/api/v1/user/",
			                                                                   "/api/v1/articles", "/api/v1/article/",
			                                                                   "/api/v1/articles/labels")
			                                                  .hasRole("USER")
			                                                  .anyRequest().permitAll()).sessionManagement(
			    sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			                                          .sessionAuthenticationStrategy(
				                                          tokenCookieSessionAuthenticationStrategy)).csrf(
			    csrf -> csrf.csrfTokenRepository(new CookieCsrfTokenRepository())
			                .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
			                .sessionAuthenticationStrategy(((authentication, request, response) -> {})));

		return http.build();
	}

	@Bean
	public TokenCookieJweStringSerializer tokenCookieJweStringSerializer(
		@Value("${jwt.cookie-token-key}") String cookieKey)
		throws Exception {
		return new TokenCookieJweStringSerializer(
			new DirectEncrypter(OctetSequenceKey.parse(cookieKey)));
	}

	@Bean
	public TokenCookieAuthenticationConfigurer tokenCookieAuthenticationConfigurer(
		@Value("${jwt.cookie-token-key}") String cookieKey) throws Exception {
		return new TokenCookieAuthenticationConfigurer().tokenCookieStringDeserializer(
			new TokenCookieJweStringDeserializer(
				new DirectDecrypter(OctetSequenceKey.parse(cookieKey))));
	}

	@Bean
	public UserDetailsService userDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
		return username -> {
			User userByNickname = userRepository.findByNickname(username).orElse(null);
			if (userByNickname == null) {
				return null;
			}
			List<SimpleGrantedAuthority> grantedAuthorities =
				userByNickname.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName().toString()))
				              .toList();
			return org.springframework.security.core.userdetails.User.builder().username(userByNickname.getNickname())
			                                                         .password(userByNickname.getPassword())
			                                                         .authorities(grantedAuthorities).build();
		};
	}
}
