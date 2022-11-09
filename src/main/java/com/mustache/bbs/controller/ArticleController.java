package com.mustache.bbs.controller;

import com.mustache.bbs.domain.dto.ArticleDto;
import com.mustache.bbs.domain.entity.Article;
import com.mustache.bbs.repository.ArticleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/list")
    public String list(Model model){
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "articles/list";
    }

    @GetMapping("")
    public String index(){
        return "redirect:/articles/list";
    }

    @GetMapping("/new")
    public String createPage(){
        return "articles/new";
    }

    @GetMapping("/{id}")
    public String selectSingle(@PathVariable Long id, Model model) {
        Optional<Article> optionalArticle = articleRepository.findById(id);

        if (!optionalArticle.isEmpty()) {
            model.addAttribute("article", optionalArticle.get());
            return "articles/show";
        } else{
            return "articles/error";
        }

    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Article> optionalArticle = articleRepository.findById(id);
        if (!optionalArticle.isEmpty()) {
            model.addAttribute("article", optionalArticle.get());
            return "articles/edit";
        } else{
            model.addAttribute("message", String.format("%d가 없습니다.", id));
            return "articles/error";
        }
    }


    @PostMapping("")
    public String createArticle(ArticleDto form) {
        log.info(form.toString());
        Article article = form.toEntity();
        articleRepository.save(article);
        return String.format("redirect:/articles/%d", article.getId());
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable Long id, ArticleDto  articleDto, Model model){
        log.info("title:{} content:{}", articleDto.getTitle(), articleDto.getContent());
        Article article = articleRepository.save(articleDto.toEntity());
        model.addAttribute("article", article);
        return String.format("redirect:/articles/%d", article.getId());
    }
}
