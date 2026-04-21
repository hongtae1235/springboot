package me.scpark.springdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor // new UpdateArticleRequest('title', 'content');
@NoArgsConstructor // new UpdateArticleRequest();
@Getter // UpdateArticleRequest.getContent();
public class UpdateArticleRequest {
    private String title;
    private String content;
}