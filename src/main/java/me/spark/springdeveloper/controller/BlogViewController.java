package me.spark.springdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.spark.springdeveloper.dao.Article;
import me.spark.springdeveloper.dto.ArticleResponse;
import me.spark.springdeveloper.dto.ArticleViewResponse;
import me.spark.springdeveloper.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {
    // @Autowired
    private final BlogService blogService;

    // todo :  게시글 목록 뷰를 만들어 주는 메서드
    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleResponse> articles =
                blogService.findAll().stream().map(ArticleResponse::new).toList();

        model.addAttribute("articles", articles);
        return "articles"; // src/main/resources/templates/articles.html
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));
        return "article"; // src/main/resources/templates/article.html
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }
        return "new-article"; // src/main/resources/templates/new-article.html
    }
}
