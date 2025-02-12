package com.oneblog.auth.jwt;

import com.oneblog.user.User;
import com.oneblog.user.UserService;
import com.oneblog.user.role.Role;
import com.oneblog.user.role.RoleName;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtFilterTest {

	@InjectMocks
	private JwtFilter jwtFilter;
	@InjectMocks
	private SecurityContextHolder securityContextHolder;

	@Mock
	private final MockHttpServletRequest request = new MockHttpServletRequest();
	@Mock
	private final MockHttpServletResponse response = new MockHttpServletResponse();
	@Mock
	private final MockFilterChain chain = new MockFilterChain();
	@Mock
	private JwtService jwtService;
	@Mock
	private UserService userService;

	private final User user = new User(1L, "Dima", "test", "123", "123", List.of(new Role(1L, RoleName.ROLE_ADMIN,
	                                                                                      null)), null, null, null);
	private final String header = "Bearer test";
	private final String token = "test";

	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	@Test
	void doFilterInternal_WrongToken() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn(header);
		when(jwtService.extractUsername(token)).thenReturn(null);


		jwtFilter.doFilterInternal(request, response, chain);

		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}

	@Test
	void doFilterInternal_TruthToken() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn(header);
		when(jwtService.extractUsername(token)).thenReturn(user.getUsername());
		when(userService.loadUserByUsername(user.getUsername())).thenReturn(user);
		when(jwtService.isValidAccess(token, user)).thenReturn(true);

		jwtFilter.doFilterInternal(request, response, chain);

		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
		assertThat(SecurityContextHolder.getContext().getAuthentication().getAuthorities().size()).isEqualTo(1);
		assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated()).isTrue();
	}

	@Test
	void doFilterInternal_NoToken() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn(null);

		jwtFilter.doFilterInternal(request, response, chain);

		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}

	@Test
	void doFilterInternal_TokenHasNotUsername() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn(header);
		when(jwtService.extractUsername(token)).thenReturn(null);

		jwtFilter.doFilterInternal(request, response, chain);

		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}

	@Test
	void doFilterInternal_UserNotLoaded() throws ServletException, IOException {
		when(request.getHeader("Authorization")).thenReturn(header);
		when(jwtService.extractUsername(token)).thenReturn(user.getUsername());
		when(userService.loadUserByUsername(user.getUsername())).thenReturn(null);
		when(jwtService.isValidAccess(token, null)).thenReturn(false);

		jwtFilter.doFilterInternal(request, response, chain);

		assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
	}
}

