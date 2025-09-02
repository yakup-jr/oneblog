package net.oneblog.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.RefreshTokenRequestModel;
import net.oneblog.auth.service.TokenService;
import net.oneblog.sharedconfig.test.IntegrationTest;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenService tokenService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void refreshToken_Success() throws Exception {
        RefreshTokenRequestModel request = new RefreshTokenRequestModel("valid-refresh-token");
        AuthenticationResponseModel response =
            new AuthenticationResponseModel("new-access-token", "new-refresh-token");

        when(tokenService.reIssueRefreshToken(any(RefreshTokenRequestModel.class))).thenReturn(
            response);

        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").value("new-access-token"))
            .andExpect(jsonPath("$.refreshToken").value("new-refresh-token"));
    }

    @ParameterizedTest
    @MethodSource("tokenErrorScenarios")
    void refreshToken_ErrorScenarios(String token, Exception exception,
                                     ResultMatcher expectedStatus) throws Exception {
        RefreshTokenRequestModel request = new RefreshTokenRequestModel(token);

        when(tokenService.reIssueRefreshToken(any(RefreshTokenRequestModel.class)))
            .thenThrow(exception);

        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(expectedStatus);
    }

    private static Stream<Arguments> tokenErrorScenarios() {
        return Stream.of(
            Arguments.of("invalid-refresh-token", new ServiceException("Invalid token"),
                status().isBadRequest()),
            Arguments.of("expired-refresh-token", new ServiceException("Token expired"),
                status().isBadRequest()),
            Arguments.of("", new ServiceException("Token cannot be empty"),
                status().isBadRequest()),
            Arguments.of(null, new ServiceException("Token cannot be null"),
                status().isBadRequest()),
            Arguments.of("valid-token-but-user-not-found",
                new UserNotFoundException("User not found"), status().isNotFound())
        );
    }
}
