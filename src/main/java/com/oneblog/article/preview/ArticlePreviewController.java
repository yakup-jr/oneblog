package com.oneblog.article.preview;

import com.oneblog.article.preview.dto.ArticlePreviewDto;
import com.oneblog.exceptions.ApiRequestException;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/article/preview")
public class ArticlePreviewController {

	@GetMapping("/{articlePreviewId}")
	public ResponseEntity<EntityModel<ArticlePreviewDto>> findByArticlePreviewId(
		@PathVariable("articlePreviewId") Long articlePreviewId) {
		throw new ApiRequestException("Not implemented yet");
	}

}
