package com.oneblog.article;

import com.oneblog.article.label.LabelNotFoundException;
import com.oneblog.article.label.LabelService;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.user.UserNotFoundException;
import com.oneblog.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

	private final ArticleRepository articleRepository;
	private final UserService userService;
	private final LabelService labelService;

	public ArticleServiceImpl(
		ArticleRepository articleRepository, UserService userService, LabelService labelService) {
		this.articleRepository = articleRepository;
		this.userService = userService;
		this.labelService = labelService;
	}

	@Override
	public Article save(Article article) throws ApiRequestException, UserNotFoundException, LabelNotFoundException {
		if (article.getPreview() == null) {
			throw new ApiRequestException("Preview could not be null");
		}
		if (article.getLabels() == null || article.getLabels().isEmpty()) {
			throw new LabelNotFoundException("Label could not be empty");
		}
		if (article.getUser() == null || article.getUser().getUserId() == null) {
			throw new UserNotFoundException("User could not be null");
		}
		article.setLabels(labelService.findLabels(article.getLabels()));
		article.setUser(userService.findById(article.getUser().getUserId()));
		return articleRepository.save(article);
	}

	@Override
	public Article findByArticleId(Long id) throws ArticleNotFoundException {
		return articleRepository.findById(id).orElseThrow(
			() -> new ArticleNotFoundException("Article with id: " + id + " not found"));
	}

	@Override
	public List<Article> findByUserId(Long userId) throws ArticleNotFoundException {
		List<Article> articles = articleRepository.findByUserId(userId);
		if (articles.isEmpty()) {
			throw new ArticleNotFoundException("Article with user id: " + userId + " not found");
		}
		return articles;
	}

	@Override
	public void deleteByArticleId(Long id) throws ArticleNotFoundException {
		if (!articleRepository.existsById(id)) {
			throw new ArticleNotFoundException("Article with id: " + id + " not found");
		}
		articleRepository.deleteById(id);
	}

	@Override
	public void deleteByUserId(Long userId) {
		if (!userService.existsById(userId)) {
			throw new UserNotFoundException("User with id: " + userId + " not found");
		}
		if (!articleRepository.existsByUserId(userId)) {
			throw new ArticleNotFoundException("Articles with user id: " + userId + " not found");
		}
		articleRepository.deleteByUserId(userId);
	}
}
