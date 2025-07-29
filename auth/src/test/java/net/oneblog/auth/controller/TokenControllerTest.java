package net.oneblog.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.oneblog.auth.models.AuthenticationResponseModel;
import net.oneblog.auth.models.RefreshTokenRequestModel;
import net.oneblog.auth.service.TokenService;
import net.oneblog.sharedconfig.test.IntegrationTest;
import net.oneblog.sharedexceptions.ServiceException;
import net.oneblog.user.exceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void refreshToken_InvalidToken() throws Exception {
        RefreshTokenRequestModel request = new RefreshTokenRequestModel("invalid-refresh-token");

        when(tokenService.reIssueRefreshToken(any(RefreshTokenRequestModel.class)))
            .thenThrow(new ServiceException("Invalid token"));

        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void refreshToken_UserNotFound() throws Exception {
        RefreshTokenRequestModel request =
            new RefreshTokenRequestModel("valid-token-but-user-not-found");

        when(tokenService.reIssueRefreshToken(any(RefreshTokenRequestModel.class)))
            .thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isNotFound());
    }

    @Test
    void refreshToken_ExpiredToken() throws Exception {
        RefreshTokenRequestModel request = new RefreshTokenRequestModel("expired-refresh-token");

        when(tokenService.reIssueRefreshToken(any(RefreshTokenRequestModel.class)))
            .thenThrow(new ServiceException("Token expired"));

        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void refreshToken_EmptyToken() throws Exception {
        RefreshTokenRequestModel request = new RefreshTokenRequestModel("");

        when(tokenService.reIssueRefreshToken(any(RefreshTokenRequestModel.class)))
            .thenThrow(new ServiceException("Token cannot be empty"));

        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void refreshToken_NullToken() throws Exception {
        RefreshTokenRequestModel request = new RefreshTokenRequestModel(null);

        when(tokenService.reIssueRefreshToken(any(RefreshTokenRequestModel.class)))
            .thenThrow(new ServiceException("Token cannot be null"));

        mockMvc.perform(post("/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());
    }
}
