package me.scpark.springdeveloper;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import me.scpark.springdeveloper.dao.Article;
import me.scpark.springdeveloper.dto.AddArticleRequest;
import me.scpark.springdeveloper.dto.UpdateArticleRequest;
import me.scpark.springdeveloper.repository.BlogRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BlogApiControllerTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 객체를 -> JSON 문자열로 변환

    @Autowired
    protected BlogRepository blogRepository;

    @BeforeEach
    public void deleteAll(){
        blogRepository.deleteAll();
    }

    @DisplayName("addArticle: 블로그 글 추가에 성공한다.")
    @Test
    public void addArticle() throws Exception {
        // given
        final String url = "/api/articles";
        final String title = "테스트";
        final String content = "블로그 글 첫 번째 입니다.";
        final AddArticleRequest article = new AddArticleRequest(title, content);
        final String requestBody = objectMapper.writeValueAsString(article);

        // when
        ResultActions result =  mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody)); // Http Request 보내는 것을 흉내낸다.

        // then

        result.andExpect(status().isCreated());
        List<Article> articles =  blogRepository.findAll();
        assertThat(articles.size()).isEqualTo(1);
        assertThat(articles.get(0).getTitle()).isEqualTo(title);
        assertThat(articles.get(0).getContent()).isEqualTo(content);

    }

    @DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
    @Test
    public void findAllArticles() throws Exception {
        // given :
        //  blog Repository.save(Article.builder().title("title).content("content").build());
        final  String url = "/api/articles";
        blogRepository.save(Article.builder().title("title").content("content").build());

        // when : get 방식으로 /api/articles
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then : status OK이고 읽어온 데이터의 내용이 내가 삽입한 내용과 동일하다.
        /*
         {
            "title": "제목 1",
            "content": "내용 1".
         }
        */
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("content"))
                .andExpect(jsonPath("$[0].title").value("title"));
        /*
         {
            "title": "안녕하세요",
            "content": "반갑습니다".
         // -> jsonPath("$.title"), jsonPath("$.content");
        */
    }

    @DisplayName("findArticle : 블로그 글 조회에 성공했다.")
    @Test
    public void findArticle() throws Exception {

        //given (데이터 준비 : 블로그 글 하나 생성)
        final String url ="/api/articles/{id}";
        final String title = "블로그 제목";
        final String content = "블로그 내용";

        Article savedArticle =  blogRepository.save(Article.builder().title(title).content(content).build());
        //when (실행 : 위에서 생성된 블로그 글을 조회)
        final ResultActions resultActions = mockMvc.perform(get(url, savedArticle.getId()));

        //then (검증 : status가 200이고 조회한 블로그 제목과 내용이 위에서 삽입한 그것과 동일한 지 확인)
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.content").value(content));
    }
    @DisplayName("deleteArticle: 블로그 글 삭제를 성공한다")
    @Test
    public void deleteArticle() throws Exception {
        final String url = "/api/articles/{id}";
        final String title = "4월 16일";
        final String content = "백엔드프로그래밍(II)";
        Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

        mockMvc.perform(delete(url, savedArticle.getId())).andExpect(status().isOk());

        List<Article> articles =  blogRepository.findAll();
        assertThat(articles).isEmpty();

    }

    @DisplayName("updateArticle: 블로그 글 수정에 성공한다")
    @Test
    public void updateArticle() throws Exception {
        // given:레코드 생성, 변경 내용 작성
        final String url = "/api/articles/{id}";
        final String title = "JUnit에서 제목 변경";
        final String content = "JUnit에서 내용 변경";
        Article savedArticle =  blogRepository.save(Article.builder().title(title).content(content).build());


        final String newTitle = "JUnit에서 제목 변경";
        final String newContent = "JUnit에서 내용 변경";
        UpdateArticleRequest request = new UpdateArticleRequest(newTitle, newContent);

        // when: /api/articles/ 생성된 레코드 id -> put 방식 요청
        ResultActions result =mockMvc.perform(put(url, savedArticle.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(request)));
        // then: staus code가 200, repository에서 변경된 내용 검증
        result.andExpect(status().isOk());
        Article article =  blogRepository.findById(savedArticle.getId()).get();
        assertThat(article.getTitle()).isEqualTo(newTitle);
        assertThat(article.getContent()).isEqualTo(newContent);
    }

}