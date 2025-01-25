package com.oneblog.cookie;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;

public class TokenAuthenticationUserDetailsService
	implements AuthenticationUserDetailsService<PreAuthenticatedAuthenticationToken> {


	@Override
	public UserDetails loadUserDetails(PreAuthenticatedAuthenticationToken authenticationToken)
		throws UsernameNotFoundException {
		if (authenticationToken.getPrincipal() instanceof Token token) {
			return new TokenUser(token.subject(), "nopassword", true, true,
			                     true, true,
			                     token.authorities().stream().map(SimpleGrantedAuthority::new).toList(), token);
		}

		throw new UsernameNotFoundException("Principal must be of type Token");
	}
}
