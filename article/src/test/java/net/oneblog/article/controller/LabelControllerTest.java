package net.oneblog.article.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.oneblog.sharedconfig.test.IntegrationTest;
import net.oneblog.api.interfaces.LabelName;
import net.oneblog.sharedconfig.test.WithMockAdmin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@IntegrationTest
@EnableAutoConfiguration(exclude = {SecurityAutoConfiguration.class})
public class LabelControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void saveLabel_ReturnLabel() throws Exception {
        mockMvc.perform(post("/api/v1/articles/label").contentType(MediaType.APPLICATION_JSON)
                .content("   {\n" + "       \"name\": \"Rust\"\n" + "   }\n"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name", is(LabelName.Rust.toString())))
            .andExpect(jsonPath("$._links.label.href", containsString("api/v1/articles/label/1")));
    }

    @Test
    public void saveLabel_ThrowException() throws Exception {
        mockMvc.perform(post("/api/v1/articles/label").contentType(MediaType.APPLICATION_JSON)
                .content("   {\n" + "       \"name\": \"Python\"\n" + "   }\n"))
            .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", notNullValue()));
    }

    @Test
    void saveLabel_ThrowMethodArgumentNotValidException_NameNull() throws Exception {
        mockMvc.perform(
                post("/api/v1/articles/label").contentType(MediaType.APPLICATION_JSON).content("""
                    	{
                    		"name": null
                    	}
                    """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", containsString("name: must not be null")));
    }

    @Test
    void saveLabel_ThrowMethodArgumentNotValidException_NameBlank() throws Exception {
        mockMvc.perform(
                post("/api/v1/articles/label").contentType(MediaType.APPLICATION_JSON).content("""
                    	{
                    		"name": ""
                    	}
                    """))
            .andExpect(status().isBadRequest());
    }

    @Test
    public void findByLabelId_ReturnLabel() throws Exception {
        mockMvc.perform(get("/api/v1/articles/label/1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name", is(LabelName.Assembler.toString())))
            .andExpect(jsonPath("$._links.self.href", containsString("api/v1/articles/label/1")))
            .andExpect(jsonPath("$._links.labels.href", containsString("labels")));
    }

    @Test
    public void findByLabelId_ThrowException() throws Exception {
        mockMvc.perform(get("/api/v1/articles/label/999").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void findByLabelName_ReturnLabel() throws Exception {
        mockMvc.perform(
                get("/api/v1/articles/label/name/Java").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.name", is(LabelName.Java.toString())))
            .andExpect(
                jsonPath("$._links.self.href", containsString("api/v1/articles/label/name/Java")))
            .andExpect(jsonPath("$._links.labels.href", containsString("labels")));
    }

    @Test
    public void findByLabelName_ThrowException() throws Exception {
        mockMvc.perform(
                get("/api/v1/articles/label/name/Wrong").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    public void findAll_ReturnsLabels() throws Exception {
        mockMvc.perform(
                get("/api/v1/articles/labels?page=0&size=5").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.labels", hasSize(5)))
            .andExpect(jsonPath("$._embedded.labels[0].labelId", is(1)))
            .andExpect(jsonPath("$._embedded.labels[0].name", is("Assembler")))
            .andExpect(jsonPath("$._embedded.labels[0]._links.self.href", endsWith("/label/1")));
    }

    @Test
    void findAll_ReturnBadRequest_PageNull() throws Exception {
        mockMvc.perform(get("/api/v1/articles/labels").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_ReturnBadRequest_PageLessZero() throws Exception {
        mockMvc.perform(
                get("/api/v1/articles/labels?page=-1").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_ReturnBadRequest_PageMoreMax() throws Exception {
        mockMvc.perform(
                get("/api/v1/articles/labels?page=999").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void findAll_ReturnBadRequest_SizeLessOne() throws Exception {
        mockMvc.perform(
                get("/api/v1/articles/labels?size=0").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void findAll_ReturnBadRequest_SizeMoreMax() throws Exception {
        mockMvc.perform(
                get("/api/v1/articles/labels?size=999").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById_ReturnLabel() throws Exception {
        mockMvc.perform(delete("/api/v1/articles/label/5").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk()).andExpect(jsonPath("$.name", notNullValue()))
            .andExpect(
                jsonPath(("$._links.label.href"), containsString("api/v1/articles/label/5")));
    }

    @Test
    void deleteById_ThrowException() throws Exception {
        mockMvc.perform(
                delete("/api/v1/articles/label/999").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }
}
