package com.oneblog.cookie;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.logout.CookieClearingLogoutHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;

import java.util.function.Function;
import java.util.logging.Logger;

public class TokenCookieAuthenticationConfigurer
	extends AbstractHttpConfigurer<TokenCookieAuthenticationConfigurer, HttpSecurity> {

	private final Logger logger = Logger.getLogger(TokenCookieAuthenticationConfigurer.class.getName());
	private Function<String, Token> tokenCookieStringDeserializer;

	@Override
	public void init(HttpSecurity builder) throws Exception {
		builder.logout(logout -> {
			logout.addLogoutHandler(new CookieClearingLogoutHandler("__Host-auth-token"))
			      .addLogoutHandler((request, response, authentication) -> {
				      if (authentication != null &&
				          authentication.getPrincipal() instanceof TokenUser user) {
				      }

				      response.setStatus(HttpServletResponse.SC_NO_CONTENT);
			      });
		});
	}

	@Override
	public void configure(HttpSecurity builder) throws Exception {
		AuthenticationFilter cookieAuthenticationFilter =
			new AuthenticationFilter(builder.getSharedObject(AuthenticationManager.class),
			                         new TokenCookieAuthenticationConverter(tokenCookieStringDeserializer));
		cookieAuthenticationFilter.setSuccessHandler((request, response, authentication) -> {});
		cookieAuthenticationFilter.setFailureHandler(
			new AuthenticationEntryPointFailureHandler(new Http403ForbiddenEntryPoint()));

		PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();

		builder.addFilterAfter(cookieAuthenticationFilter, CsrfFilter.class)
		       .authenticationProvider(authenticationProvider);
	}


	public TokenCookieAuthenticationConfigurer tokenCookieStringDeserializer(
		Function<String, Token> tokenCookieStringDeserializer) {
		this.tokenCookieStringDeserializer = tokenCookieStringDeserializer;
		return this;
	}
}
