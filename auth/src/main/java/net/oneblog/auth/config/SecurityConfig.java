package net.oneblog.auth.config;

import net.oneblog.auth.filter.JwtFilter;
import net.oneblog.auth.handler.CustomAccessDeniedHandler;
import net.oneblog.auth.handler.CustomLogoutHandler;
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
import org.springframework.security.core.userdetails.UserDetailsService;
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

/**
 * The type Security config.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFIlter;

    private final UserDetailsService userDetailsService;

    private final CustomAccessDeniedHandler accessDeniedHandler;

    private final CustomLogoutHandler customLogoutHandler;

    /**
     * Instantiates a new Security config.
     *
     * @param jwtFIlter           the jwt f ilter
     * @param accessDeniedHandler the access denied handler
     * @param customLogoutHandler the custom logout handler
     */
    public SecurityConfig(
        JwtFilter jwtFIlter, UserDetailsService userDetailsService,
        CustomAccessDeniedHandler accessDeniedHandler,
        CustomLogoutHandler customLogoutHandler) {
        this.jwtFIlter = jwtFIlter;
        this.userDetailsService = userDetailsService;
        this.accessDeniedHandler = accessDeniedHandler;
        this.customLogoutHandler = customLogoutHandler;
    }

    /**
     * Security filter chain security filter chain.
     *
     * @param http the http
     * @return the security filter chain
     * @throws Exception the exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
        HttpSecurity http) throws Exception {

        System.out.println("I'm here bitch");

        http
            .csrf(AbstractHttpConfigurer::disable)
            .headers(headers ->
                headers.contentSecurityPolicy(
                        csp -> csp.policyDirectives("script-src 'self' https://accounts.google.com"))
                    .crossOriginOpenerPolicy(
                        coop -> coop.policy(
                            CrossOriginOpenerPolicyHeaderWriter.CrossOriginOpenerPolicy.SAME_ORIGIN))
                    .crossOriginEmbedderPolicy(
                        coep -> coep.policy(
                            CrossOriginEmbedderPolicyHeaderWriter.CrossOriginEmbedderPolicy.REQUIRE_CORP))
                    .frameOptions(
                        HeadersConfigurer.FrameOptionsConfig::sameOrigin))
            .cors(configurer -> configurer.configurationSource(apiConfigurationSource()))
            .sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .userDetailsService(userDetailsService).exceptionHandling(e -> {
                e.accessDeniedHandler(accessDeniedHandler);
                e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
            }).addFilterBefore(jwtFIlter, UsernamePasswordAuthenticationFilter.class).logout(log -> {
                log.logoutUrl("/logout");
                log.addLogoutHandler(customLogoutHandler);
                log.logoutSuccessHandler(
                    (request, response, authentication) -> SecurityContextHolder.getContext());
            }).authorizeHttpRequests(
                authorizeHttpRequests -> authorizeHttpRequests.requestMatchers(HttpMethod.POST,
                        "/api/v1/user",
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
                    .anyRequest().permitAll());

        return http.build();
    }

    /**
     * Password encoder password encoder.
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication manager bean authentication manager.
     *
     * @param config the config
     * @return the authentication manager
     * @throws Exception the exception
     */
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration config)
        throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Api configuration source url based cors configuration source.
     *
     * @return the url based cors configuration source
     */
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
