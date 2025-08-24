package net.oneblog.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.models.BasicRegistrationRequestModel;
import net.oneblog.auth.models.LoginRequestModel;
import net.oneblog.auth.repository.AuthRepository;
import net.oneblog.email.models.RegistrationEmailVerificationModel;
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

import static org.assertj.core.api.Assertions.assertThat;
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
    private AuthRepository authRepository;

    @MockBean
    private CodeGenerator codeGenerator;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register_Success() throws Exception {
        BasicRegistrationRequestModel request =
            new BasicRegistrationRequestModel("testname", "testuser", "test@example.com", "password123");

        when(codeGenerator.generateSixDigits()).thenReturn("123456");

        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isCreated());
    }

    @Test
    void register_UsernameAlreadyTaken() throws Exception {
        BasicRegistrationRequestModel request =
            new BasicRegistrationRequestModel("testname", "shadow", "test@example.com ",
                "password123");

        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    void register_EmailAlreadyTaken() throws Exception {
        BasicRegistrationRequestModel request =
            new BasicRegistrationRequestModel("testname", "testuser", "shadow@mail.com",
                "password123");

        mockMvc.perform(post("/registration").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    void verifyEmail_Success() throws Exception {
        String generatedCode = "123456";
        when(codeGenerator.generateSixDigits()).thenReturn(generatedCode);

        BasicRegistrationRequestModel registerRequest =
            new BasicRegistrationRequestModel("verifyEmail", "verifyUser", "verify@example.com",
                "password123");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
            .andExpect(status().isCreated());

        RegistrationEmailVerificationModel verifyRequest =
            new RegistrationEmailVerificationModel("verify@example.com", generatedCode);

        mockMvc.perform(post("/registration/email/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyRequest)))
            .andExpect(status().isOk())
            .andDo(result -> {
                AuthEntity auth = authRepository.findByUserEntityEmail("verify@example.com").get();
                assertThat(auth.getUserEntity().getEmail()).isEqualTo("verify@example.com");
            });
    }


    @Test
    void verifyEmail_InvalidCode() throws Exception {
        RegistrationEmailVerificationModel request =
            new RegistrationEmailVerificationModel("test@example.com", "invalid");

        mockMvc.perform(post("/registration/email/verify").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isBadRequest());
    }

    @Test
    @Disabled
    void login_Success() throws Exception {
        LoginRequestModel request = new LoginRequestModel("shadow", "strongPass2");

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
        LoginRequestModel request = new LoginRequestModel("nonexistent", "password123");

        mockMvc.perform(post("/login/basic").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))).andExpect(status().isNotFound());
    }
}
