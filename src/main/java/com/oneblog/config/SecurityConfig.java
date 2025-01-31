package com.oneblog.config;

import com.oneblog.auth.CustomAccessDeniedHandler;
import com.oneblog.auth.CustomLogoutHandler;
import com.oneblog.auth.JwtFilter;
import com.oneblog.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.CrossOriginEmbedderPolicyHeaderWriter;
import org.springframework.security.web.header.writers.CrossOriginOpenerPolicyHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

	private final JwtFilter jwtFIlter;

	private final UserService userService;

	private final CustomAccessDeniedHandler accessDeniedHandler;

	private final CustomLogoutHandler customLogoutHandler;

	public SecurityConfig(
		JwtFilter jwtFIlter, UserService userService, CustomAccessDeniedHandler accessDeniedHandler,
		CustomLogoutHandler customLogoutHandler) {
		this.jwtFIlter = jwtFIlter;
		this.userService = userService;
		this.accessDeniedHandler = accessDeniedHandler;
		this.customLogoutHandler = customLogoutHandler;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(
		HttpSecurity http) throws Exception {

		http.csrf(AbstractHttpConfigurer::disable)
		    .headers(headers -> headers
			                        .contentSecurityPolicy(csp -> csp
				                                                      .policyDirectives(
					                                                      "script-src 'self' https://accounts.google.com"))
			                        .crossOriginOpenerPolicy(coop -> coop
				                                                         .policy(
					                                                         CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN))
			                        .crossOriginEmbedderPolicy(coep -> coep
				                                                           .policy(
					                                                           CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy.REQUIRE_CORP))
			                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
		    )
		    .cors(configurer -> configurer.configurationSource(apiConfigurationSource()))
		    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
		    .userDetailsService(userService)
		    .exceptionHandling(e -> {
			    e.accessDeniedHandler(accessDeniedHandler);
			    e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		    })
		    .addFilterBefore(jwtFIlter, UsernamePasswordAuthenticationFilter.class).logout(log -> {
			    log.logoutUrl("/logout");
			    log.addLogoutHandler(customLogoutHandler);
			    log.logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.getContext());
		    }).authorizeHttpRequests(
			    authorizeHttpRequests -> authorizeHttpRequests.requestMatchers(HttpMethod.POST, "/api/v1/user",
			                                                                   "/api/v1/article/", "/api/v1/articles/label")
			                                                  .hasRole("ADMIN")
			                                                  .requestMatchers(HttpMethod.GET, "/api/v1/users")
			                                                  .hasRole("ADMIN")
			                                                  .requestMatchers(HttpMethod.DELETE, "/api/v1/user/{userId}",
			                                                                   "/api/v1/article/{articleId}",
			                                                                   "/api/v1/articles/label/{labelId}")
			                                                  .hasRole("ADMIN")
			                                                  .requestMatchers(HttpMethod.GET, "/api/v1/user/",
			                                                                   "/api/v1/articles", "/api/v1/article/",
			                                                                   "/api/v1/articles/labels").hasRole("USER")
			                                                  .requestMatchers("/greeting").authenticated().anyRequest()
			                                                  .permitAll());

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	UrlBasedCorsConfigurationSource apiConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowCredentials(true);
		configuration.setAllowedOrigins(List.of("http://localhost:4200"));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(
			Arrays.asList("Authorization", "Content-Type", "Accept", "Origin", "X-Requested-With"));
		configuration.setExposedHeaders(List.of("Authorization"));
		configuration.setMaxAge(3600L);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
