package com.oneblog.article.label;


import com.oneblog.config.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = {TestConfig.class})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql("/static/testdb/data.sql")
public class LabelControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void saveLabel_ReturnLabel() throws Exception {
		mockMvc.perform(post("/api/v1/articles/label").contentType(MediaType.APPLICATION_JSON)
		                                              .content("   {\n" + "       \"name\": \"Rust\"\n" + "   }\n"))
		       .andExpect(status().isCreated()).andExpect(jsonPath("$.name", is(LabelName.Rust.toString())))
		       .andExpect(jsonPath("$._links.label.href", containsString("api/v1/articles/label/1")));
	}

	@Test
	public void saveLabel_ThrowException() throws Exception {
		mockMvc.perform(post("/api/v1/articles/label").contentType(MediaType.APPLICATION_JSON)
		                                              .content("   {\n" + "       \"name\": \"Python\"\n" + "   }\n"))
		       .andExpect(status().isBadRequest()).andExpect(jsonPath("$.message", notNullValue()));
	}

	@Test
	public void findByLabelId_ReturnLabel() throws Exception {
		mockMvc.perform(get("/api/v1/articles/label/10").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$.name", is(LabelName.Assembler.toString())))
		       .andExpect(jsonPath("$._links.self.href", containsString("api/v1/articles/label/10")))
		       .andExpect(jsonPath("$._links.labels.href", containsString("api/v1/articles/labels")));
	}

	@Test
	public void findByLabelId_ThrowException() throws Exception {
		mockMvc.perform(get("/api/v1/articles/label/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	public void findByLabelName_ReturnLabel() throws Exception {
		mockMvc.perform(get("/api/v1/articles/label/name/Java").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$.name", is(LabelName.Java.toString())))
		       .andExpect(jsonPath("$._links.self.href", containsString("api/v1/articles/label/name/Java")))
		       .andExpect(jsonPath("$._links.labels.href", containsString("api/v1/articles/labels")));
	}

	@Test
	public void findByLabelName_ThrowException() throws Exception {
		mockMvc.perform(get("/api/v1/articles/label/name/Wrong").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}

	@Test
	public void findAll_ReturnsLabels() throws Exception {
		mockMvc.perform(get("/api/v1/articles/labels").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$._embedded.labelDtoList", hasSize(9)))
		       .andExpect(jsonPath("$._links.self.href", containsString("api/v1/articles/labels")));
	}

	@Test
	public void deleteById_ReturnLabel() throws Exception {
		mockMvc.perform(delete("/api/v1/articles/label/10").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isOk()).andExpect(jsonPath("$.name", is(LabelName.Assembler.toString())))
		       .andExpect(jsonPath(("$._links.label.href"), containsString("api/v1/articles/label/10")));
	}

	@Test
	public void deleteById_ThrowException() throws Exception {
		mockMvc.perform(delete("/api/v1/articles/label/999").contentType(MediaType.APPLICATION_JSON))
		       .andExpect(status().isNotFound());
	}
}
