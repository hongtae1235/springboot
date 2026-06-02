package me.scpark.springdeveloper.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import me.scpark.springdeveloper.dao.Article;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ArticleViewResponse {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ArticleViewResponse(Article article){
        this.id=article.getId();
        this.title=article.getTitle();
        this.content=article.getContent();
        this.createdAt=article.getCreateAt();
        this.updatedAt=article.getUpdatedAt();
    }
}