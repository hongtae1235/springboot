package me.scpark.springdeveloper.repository;

import me.scpark.springdeveloper.dao.Article;
import org.springframework.data.jpa.repository.JpaRepository; // 추가
import org.springframework.stereotype.Repository; // 추가

@Repository // 스프링에게 이 인터페이스가 리포지토리임을 알려줍니다.
public interface BlogRepository extends JpaRepository<Article, Long> {
    // 이제 save() 같은 메서드를 직접 만들지 않아도 JpaRepository가 제공해줍니다.
    // 기존에 작성하신 Article save(Article entity); 는 삭제하셔도 무방합니다.
}