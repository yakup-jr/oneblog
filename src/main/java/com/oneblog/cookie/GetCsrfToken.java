package com.oneblog.cookie;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Setter
public class GetCsrfToken extends OncePerRequestFilter {

	private RequestMatcher requestMatcher = new AntPathRequestMatcher("/api/v1/csrf-token", HttpMethod.GET.name());

	private CsrfTokenRepository csrfTokenRepository = new CookieCsrfTokenRepository();

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		if (requestMatcher.matches(request)) {
			response.setStatus(HttpServletResponse.SC_OK);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			objectMapper.writeValue(response.getWriter(),
			                        csrfTokenRepository.loadDeferredToken(request, response).get());
			return;
		}

		filterChain.doFilter(request, response);
	}
}
