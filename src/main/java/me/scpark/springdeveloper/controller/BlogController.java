package me.scpark.springdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.scpark.springdeveloper.dao.Article;
import me.scpark.springdeveloper.dto.AddArticleRequest;
import me.scpark.springdeveloper.dto.ArticleResponse;
import me.scpark.springdeveloper.dto.UpdateArticleRequest;
import me.scpark.springdeveloper.service.BlogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class BlogController {
    private final BlogService blogService;

    @PostMapping("/api/articles")
    public ResponseEntity<Article>addArticle(@RequestBody AddArticleRequest addArticleRequest){
        Article article = blogService.save(addArticleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(article);

    }

    @GetMapping("/api/articles")
    public ResponseEntity<List<ArticleResponse>> finAllArticles(){
        List<Article> articles = blogService.findAll();
        List<ArticleResponse> result = articles.stream().map(ArticleResponse::new).toList();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/api/articles/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable long id){
        Article article = blogService.findById(id);
        return ResponseEntity.ok().body(new ArticleResponse(article));
    }

    @DeleteMapping("/api/articles/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable long id){
        blogService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/api/articles/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable long id, @RequestBody UpdateArticleRequest request){
        Article updateArticle =  blogService.update(id, request);
        return ResponseEntity.ok().body(updateArticle);
    }
}
