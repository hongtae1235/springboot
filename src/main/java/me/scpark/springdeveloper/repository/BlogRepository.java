package me.scpark.springdeveloper.repository;

import me.scpark.springdeveloper.dao.Article;

public interface BlogRepository {

    Article save(Article entity);
}
