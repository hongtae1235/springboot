package me.spark.springdeveloper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class ExampleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("thymeleaf example page renders")
    @Test
    void thymeleafExample() throws Exception {
        mockMvc.perform(get("/thymeleaf/example"))
                .andExpect(status().isOk())
                .andExpect(view().name("example"))
                .andExpect(model().attributeExists("name"))
                .andExpect(model().attribute("articles", hasSize(10)))
                .andExpect(content().string(containsString("<table>")))
                .andExpect(content().string(containsString("<ul>")))
                .andExpect(content().string(containsString("운동")))
                .andExpect(content().string(containsString("독서")));
    }
}
