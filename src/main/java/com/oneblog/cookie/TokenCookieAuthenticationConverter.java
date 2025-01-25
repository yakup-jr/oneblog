package com.oneblog.cookie;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class TokenCookieAuthenticationConverter implements AuthenticationConverter {

	private final Function<String, Token> tokenCookieStringDeserializer;

	public TokenCookieAuthenticationConverter(Function<String, Token> tokenCookieStringDeserializer) {
		this.tokenCookieStringDeserializer = tokenCookieStringDeserializer;
	}

	@Override
	public Authentication convert(HttpServletRequest request) {
		if (request.getCookies() != null) {
			return Stream.of(request.getCookies()).filter(cookie -> Objects.equals(cookie.getName(),
			                                                                       "__Host-auth-token")).findFirst()
			             .map(cookie -> {
				             Token token = tokenCookieStringDeserializer.apply(cookie.getValue());
				             return new PreAuthenticatedAuthenticationToken(token, cookie.getValue());
			             }).orElse(null);
		}
		return null;
	}
}
