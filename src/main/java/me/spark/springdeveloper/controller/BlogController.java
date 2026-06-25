package me.spark.springdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.spark.springdeveloper.dao.Article;
import me.spark.springdeveloper.dto.AddArticleRequest;
import me.spark.springdeveloper.dto.ArticleResponse;
import me.spark.springdeveloper.dto.UpdateArticleRequest;
import me.spark.springdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController// 응답으로 데이터를 반환
@RequiredArgsConstructor
public class BlogController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest articleRequest) {
        Article article = blogService.save(articleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);
    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> getAllArticles() {
        List<Article> articles = blogService.findAll();
        List<ArticleResponse> result =  articles.stream().map(ArticleResponse::new).toList();
//        List<ArticleResponse> result = new ArrayList<>();
//        for (Article a : articles){
//            result.add(new ArticleResponse(a));
//        }
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/api/articles/{id}") // /api/articles/3
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) {
        Article article = blogService.findById(id);
        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public  ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id,
                                                 @RequestBody UpdateArticleRequest request){
        Article updateArticle =  blogService.update(id, request);
        return  ResponseEntity.ok().body(updateArticle);
    }
}
