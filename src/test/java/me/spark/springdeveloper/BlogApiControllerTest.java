package me.spark.springdeveloper;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.spark.springdeveloper.dao.Article;
import me.spark.springdeveloper.dto.UpdateArticleRequest;
import me.spark.springdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class BlogApiControllerTest {

    private static final String API_URL = "/api/articles";
    private static final String TITLE = "test title";
    private static final String CONTENT = "test content";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BlogRepository blogRepository;

    @BeforeEach
    @AfterEach
    void cleanUp() {
        blogRepository.deleteAll();
    }

//    @DisplayName("addArticle: 블로그 글 조회 성공")
//    @Test
//    void addArticle() throws Exception {
//        ResultActions result = createArticle(TITLE, CONTENT);
//
//        result.andExpect(status().isCreated());
//
//        List<Article> articles = blogRepository.findAll();
//        assertThat(articles).hasSize(1);
//        assertThat(articles.get(0).getTitle()).isEqualTo(TITLE);
//        assertThat(articles.get(0).getContent()).isEqualTo(CONTENT);
//    }

    @DisplayName("findAllArticles: 블로그 글 조회 성공")
    @Test
    public void findAllArticles() throws Exception {
        // given
//        blogRepository.save(new Article("title", "content"));
        final String url = "/api/articles";
        blogRepository.save(Article.builder().title("title").content("content").build());
        // when : get 방식으로 /api/articles
        final ResultActions resultActions =  mockMvc.perform(get(url).accept(MediaType.APPLICATION_JSON));
        // then " status Ok이고 읽어온 데이터의 내용이 내가 삽입한 내용과 동일하다.
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("content"))
                .andExpect(jsonPath("$[0].title").value("title"));
    }

    @DisplayName("findArticle: 블로그 글 조회에 성공한다.")
    @Test
    public void findArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final String title = "블로그 제목";
        final String content = "블로그 내용";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        // when
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedArticle.getId()))
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));
    }

    @DisplayName("deleteArticle: 블로그 글 삭제에 성공한다")
    @Test
    public void deleteArticle() throws Exception {
        // given
        final String url = "/api/articles/{id}";
        final String title = "4월 16일";
        final String content = "백엔드 프로그래밍(II) 수업";
        Article savedarticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());
        // when
        mockMvc.perform(delete(url, savedarticle.getId()))
                .andDo(print())
                .andExpect(status().isOk());
        // then
        List<Article> articles = blogRepository.findAll();
        assertThat(articles).isEmpty();

    }

    @DisplayName("UpdateArticle: 블로그 글 수정에 성공한다.")
    @Test
    public void updateArticle() throws Exception {
        // given : 레코드 생성, 변경 내용 작성
        final String url = "/api/articles/{id}";
        final String title = "title";
        final String content = "content";
        final String newTitle = "JUnit에서 제목 변경";
        final String newContent = "JUnit에서 내용 변경";

        Article savedArticle = blogRepository.save(Article.builder()
                .title(title)
                .content(content)
                .build());

        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when : /api/article/생성된 레코드 id -> put 방식 요청
        final ResultActions resultActions = mockMvc.perform(put(url, savedArticle.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then : status code가 200번, repository에서 변경된 내용 검증
        resultActions.andExpect(status().isOk());

        Article article = blogRepository.findById(savedArticle.getId()).orElseThrow();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }
}
