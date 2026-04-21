package me.scpark.springdeveloper.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.scpark.springdeveloper.dao.Article;
import me.scpark.springdeveloper.dto.AddArticleRequest;
import me.scpark.springdeveloper.dto.UpdateArticleRequest;
import me.scpark.springdeveloper.repository.BlogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
    @RequiredArgsConstructor
    public class BlogService{
        private final BlogRepository blogRepository;

        public Article save(AddArticleRequest addArticleRequest){
            return blogRepository.save(addArticleRequest.toEntity());
        }

        public List<Article> findAll(){
            return blogRepository.findAll();
        }


    @SneakyThrows
    public Article findById(long id) {
        return blogRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found"+id));
    }

    public void delete(long id) {
            blogRepository.deleteById(id);
    }

    @Transactional
    public Article update(long id, UpdateArticleRequest request) {
        Article articles = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found" + id));
        articles.update(request.getTitle(), request.getContent());
        return articles;
    }
}