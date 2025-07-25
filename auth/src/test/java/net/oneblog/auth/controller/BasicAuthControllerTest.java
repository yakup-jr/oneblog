package net.oneblog.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.oneblog.auth.dto.LoginRequestDto;
import net.oneblog.auth.dto.RegistrationRequestDto;
import net.oneblog.email.dto.RegistrationEmailVerification;
import net.oneblog.email.service.CodeGenerator;
import net.oneblog.sharedconfig.test.IntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
class BasicAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CodeGenerator codeGenerator;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_Success() throws Exception {
        RegistrationRequestDto request =
            new RegistrationRequestDto("testname", "testuser", "test@example.com", "password123");

        when(codeGenerator.generateSixDigits()).thenReturn("123456");

        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());
    }

    @Test
    void register_UsernameAlreadyTaken() throws Exception {
        RegistrationRequestDto request =
            new RegistrationRequestDto("testname", "shadow", "test@example.com ",
                "password123");

        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    void register_EmailAlreadyTaken() throws Exception {
        RegistrationRequestDto request =
            new RegistrationRequestDto("testname", "testuser", "shadow@mail.com",
                "password123");

        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    void verifyEmail_Success() throws Exception {
        when(codeGenerator.generateSixDigits()).thenReturn("123456");

        RegistrationRequestDto registerRequest =
            new RegistrationRequestDto("testname", "verifyuser", "verify@example.com",
                "password123");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isCreated());

        RegistrationEmailVerification request =
            new RegistrationEmailVerification("verify@example.com", "123456");

        mockMvc.perform(post("/registration/email/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk());
    }

    @Test
    void verifyEmail_InvalidCode() throws Exception {
        RegistrationEmailVerification request =
            new RegistrationEmailVerification("test@example.com", "invalid");

        mockMvc.perform(post("/registration/email/verify").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    @Disabled
    void login_Success() throws Exception {
        LoginRequestDto request = new LoginRequestDto("shadow", "strongPass2");

        when(passwordEncoder.encode(anyString())).thenReturn("strongPass2");

        mockMvc.perform(post("/login/basic")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void login_UserNotFound() throws Exception {
        LoginRequestDto request = new LoginRequestDto("nonexistent", "password123");

        mockMvc.perform(post("/login/basic").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound());
    }
}
