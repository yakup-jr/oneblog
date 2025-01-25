package com.oneblog.cookie;

import lombok.Setter;
import org.springframework.security.core.Authentication;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Setter
public class DefaultTokenCookieFactory implements Function<Authentication, Token> {

	private Duration tokenTtl = Duration.ofDays(1);

	@Override
	public Token apply(Authentication authentication) {
		Instant now = Instant.now();
		List<String> listAuthorities =
			authentication.getAuthorities().stream()
			              .map(grantedAuthority -> grantedAuthority.getAuthority().split("_")[1]).toList();
		return new Token(UUID.randomUUID(), authentication.getName(), listAuthorities, now, now.plus(tokenTtl));
	}
}
