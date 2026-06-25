package me.spark.springdeveloper;

import me.spark.springdeveloper.dao.Article;
import me.spark.springdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
class BlogViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BlogRepository blogRepository;

    @BeforeEach
    @AfterEach
    void cleanUp() {
        blogRepository.deleteAll();
    }

    @DisplayName("articles page renders saved articles")
    @Test
    void articlesPageRenders() throws Exception {
        blogRepository.save(Article.builder()
                .title("First article")
                .content("First content")
                .build());

        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("articles", hasSize(1)))
                .andExpect(content().string(containsString("My Blog")))
                .andExpect(content().string(containsString("First article")))
                .andExpect(content().string(containsString("First content")));
    }

    @DisplayName("article detail page renders a saved article")
    @Test
    void articlePageRenders() throws Exception {
        Article article = blogRepository.save(Article.builder()
                .title("Detail article")
                .content("Detail content")
                .build());

        mockMvc.perform(get("/articles/{id}", article.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("article"))
                .andExpect(model().attributeExists("article"))
                .andExpect(content().string(containsString("Detail article")))
                .andExpect(content().string(containsString("Detail content")));
    }

    @DisplayName("new article page renders an empty form")
    @Test
    void newArticlePageRenders() throws Exception {
        mockMvc.perform(get("/new-article"))
                .andExpect(status().isOk())
                .andExpect(view().name("new-article"))
                .andExpect(model().attributeExists("article"))
                .andExpect(content().string(containsString("등록")));
    }

    @DisplayName("new article page renders an edit form when id is provided")
    @Test
    void editArticlePageRenders() throws Exception {
        Article article = blogRepository.save(Article.builder()
                .title("Edit article")
                .content("Edit content")
                .build());

        mockMvc.perform(get("/new-article").param("id", article.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("new-article"))
                .andExpect(model().attributeExists("article"))
                .andExpect(content().string(containsString("Edit article")))
                .andExpect(content().string(containsString("Edit content")));
    }
}
