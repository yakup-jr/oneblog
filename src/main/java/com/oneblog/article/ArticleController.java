package com.oneblog.article;

import com.oneblog.article.dto.ArticleCreateDto;
import com.oneblog.article.dto.ArticleDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

	@PostMapping("/")
	public ResponseEntity<EntityModel<ArticleDto>> createArticle(@RequestBody ArticleCreateDto articleDto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}

	@GetMapping("/{articleId}")
	public ResponseEntity<EntityModel<ArticleDto>> findArticleByArticleId(@PathVariable Long articleId) {
		return null;
	}

}
