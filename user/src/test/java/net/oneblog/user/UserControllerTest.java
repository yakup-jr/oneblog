package net.oneblog.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.oneblog.sharedconfig.test.IntegrationTest;
import net.oneblog.user.dto.UserCreateDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void saveUser_ReturnUser() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto("Alex", "simple", "simple@mail.com");

        mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.userId", notNullValue()))
            .andExpect(jsonPath("$._links.user.href", endsWith("user/6")));
    }

    @Test
    void saveUser_ThrowMethodArgumentNotValidException() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto("", "", "somemail");

        mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDto)))
            .andExpect(status().isBadRequest())
            .andExpect(
                jsonPath("$.message", containsString("name: length must be between 2 and 60")))
            .andExpect(
                jsonPath("$.message", containsString("nickname: length must be between 2 and 60")))
            .andExpect(
                jsonPath("$.message",
                    containsString("email: must be a well-formed email address")));
    }

    @Test
    void saveUser_ThrowApiRequestException_NicknameIsNotUnique() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto("Alex", "shadow", "alex@mail.com");

        mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("User nickname shadow already exists")));
    }

    @Test
    void saveUser_ThrowApiRequestException_EmailIsNotUnique() throws Exception {
        UserCreateDto userCreateDto = new UserCreateDto("Alex", "simple", "shadow@mail.com");

        mockMvc.perform(post("/api/v1/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userCreateDto)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", is("User email shadow@mail.com already exists")));
    }

    @Test
    void findAllUsers_ReturnUsers() throws Exception {
        mockMvc.perform(get("/api/v1/users?page=0&size=3").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.users", hasSize(3)));
    }

    @Test
    void findAllUsers_ReturnBadRequest_PageLessZero() throws Exception {
        mockMvc.perform(get("/api/v1/users?page=-1&size=3").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void findAllUsers_ReturnNotFound_PageMoreMax() throws Exception {
        mockMvc.perform(get("/api/v1/users?page=999").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void findAllUsers_ReturnNotFound_SizeLessOne() throws Exception {
        mockMvc.perform(get("/api/v1/users?page=0&size=-1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void findAllUsers_ReturnNotFound_SizeMoreMax() throws Exception {
        mockMvc.perform(
                get("/api/v1/users?page=0&size=999").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void findUserByUserId_ReturnUser() throws Exception {
        mockMvc.perform(get("/api/v1/user/1")).andExpect(status().isOk())
            .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void findUserByUserId_ThrowUserNotFoundException() throws Exception {
        mockMvc.perform(get("/api/v1/user/999")).andExpect(status().isNotFound());
    }

    @Test
    void findUserByNickname_ReturnUser() throws Exception {
        mockMvc.perform(get("/api/v1/user/nickname/shadow")).andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname", is("shadow")));
    }

    @Test
    void findUserByNickname_ThrowUserNotFoundException() throws Exception {
        mockMvc.perform(get("/api/v1/user/nickname/unknown")).andExpect(status().isNotFound());
    }

    @Test
    void findUserByArticleId_ThrowArticleNotFoundException() throws Exception {
        mockMvc.perform(get("/api/v1/user/article/999")).andExpect(status().isNotFound());
    }
}