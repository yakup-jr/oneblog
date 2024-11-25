package com.oneblog.article;

import com.oneblog.article.label.Label;
import com.oneblog.article.label.LabelName;
import com.oneblog.article.label.LabelService;
import com.oneblog.article.preview.Preview;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.user.User;
import com.oneblog.user.UserNotFoundException;
import com.oneblog.user.UserService;
import com.oneblog.user.role.Role;
import com.oneblog.user.role.RoleName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTest {

	@Mock
	private ArticleRepository articleRepository;

	@Mock
	private UserService userService;

	@Mock
	private LabelService labelService;

	@InjectMocks
	private ArticleServiceImpl articleService;

	private static final Article defaultArticle =
		Article.builder().title("The new story").body("More and more...").articleId(1L).preview(new Preview())
		       .labels(List.of(new Label(1L, LabelName.Assembler, null), new Label(2L, LabelName.Java, null))).user(
			       new User(1L, "Dima", "yakup_jr", "somemail@mail.com", "strongpass",
			                List.of(new Role(1L, RoleName.ROLE_ADMIN, null)), null)).build();

	@Test
	public void save_ReturnArticle() {
		Article response =
			new Article(1L, defaultArticle.getTitle(), defaultArticle.getBody(), defaultArticle.getCreatedAt(),
			            defaultArticle.getPreview(), defaultArticle.getLabels(), defaultArticle.getUser());
		when(userService.findById(defaultArticle.getUser().getUserId())).thenReturn(defaultArticle.getUser());
		when(articleRepository.save(defaultArticle)).thenReturn(response);
		when(labelService.findLabels(defaultArticle.getLabels())).thenReturn(defaultArticle.getLabels());

		Article article = articleService.save(defaultArticle);

		assertThat(article).isNotNull().isInstanceOf(Article.class);
		assertThat(article.getArticleId()).isEqualTo(response.getArticleId());
	}

	@Test
	public void findByArticleId_ReturnArticle() {
		when(articleRepository.findById(1L)).thenReturn(Optional.of(defaultArticle));

		Article article = articleService.findByArticleId(1L);

		assertThat(article).isNotNull().isInstanceOf(Article.class);
		assertThat(article.getArticleId()).isEqualTo(defaultArticle.getArticleId());
	}

	@Test
	public void findByArticleId_ThrowArticleNotFoundException() {
		when(articleRepository.findById(1L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> articleService.findByArticleId(1L)).isInstanceOf(ArticleNotFoundException.class);
	}

	@Test
	void findByUserId_ReturnArticles() {
		when(articleRepository.findByUserId(1L)).thenReturn(List.of(defaultArticle, defaultArticle));

		List<Article> articles = articleService.findByUserId(1L);

		assertThat(articles).isNotNull().isInstanceOf(List.class);
		assertThat(articles.size()).isEqualTo(2);
		assertThat(articles.getFirst().getArticleId()).isEqualTo(defaultArticle.getArticleId());
	}

	@Test
	void findByUserId_ThrowArticleNotFoundException() {
		when(articleRepository.findByUserId(1L)).thenReturn(List.of());
		assertThatThrownBy(() -> articleService.findByUserId(1L)).isInstanceOf(ArticleNotFoundException.class);
	}

	@Test
	void findAll_ReturnArticles() {
		Article article1 =
			new Article(1L, "title 1", "body1", LocalDateTime.now().minusHours(1L).toLocalDate().atStartOfDay(),
			            new Preview(1L, "body 1", null), null, null);
		Article article2 =
			new Article(2L, "title 2", "body 2", LocalDateTime.now().minusHours(2L).toLocalDate().atStartOfDay(),
			            new Preview(2L, "body 2", null), null, null);
		Article article3 =
			new Article(3L, "title 3", "body 3", LocalDateTime.now(), new Preview(3L, "title 3", null), null, null);

		Pageable pageRequest = PageRequest.of(1, 3, Sort.by("createdAt").descending());

		when(articleRepository.findAll(pageRequest)).thenReturn(
			new PageImpl<>(List.of(article3, article1, article2), pageRequest, 3));

		Page<Article> articlePage = articleService.findAll(1, 3);

		assertThat(articlePage).isNotNull().isInstanceOf(Page.class);
		assertThat(articlePage.getTotalPages()).isEqualTo(2);
		assertThat(articlePage.getTotalElements()).isEqualTo(6);
		assertThat(articlePage.get().toList().size()).isEqualTo(3);
		assertThat(articlePage.get().toList().getFirst()).isEqualTo(article3);
		assertThat(articlePage.get().toList().getLast()).isEqualTo(article2);
		assertThat(articlePage.get().toList().get(1)).isEqualTo(article1);
	}

	@Test
	void findAll_ThrowApiRequestException() {
		PageRequest pageRequest = PageRequest.of(999, 3, Sort.by("createdAt").descending());

		when(articleRepository.findAll(pageRequest)).thenReturn(new PageImpl<>(List.of(), pageRequest, 0));

		assertThatThrownBy(() -> articleService.findAll(999, 3)).isInstanceOf(ApiRequestException.class);
	}

	@Test
	void deleteByArticleId_ReturnArticle() {
		when(articleRepository.existsById(1L)).thenReturn(Boolean.TRUE);

		articleService.deleteByArticleId(1L);
	}

	@Test
	void deleteByArticleId_ThrowArticleNotFoundException() {
		when(articleRepository.existsById(999L)).thenReturn(Boolean.FALSE);

		assertThatThrownBy(() -> articleService.deleteByArticleId(999L)).isInstanceOf(ArticleNotFoundException.class);
	}

	@Test
	void deleteByUserId_ReturnArticle() {
		userService.deleteById(defaultArticle.getUser().getUserId());
	}

	@Test
	void deleteByUserId_ThrowUserNotFoundException() {
		when(userService.existsById(999L)).thenReturn(Boolean.FALSE);

		assertThatThrownBy(() -> articleService.deleteByUserId(999L)).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void deleteByUserId_ThrowArticleNotFoundException() {
		when(userService.existsById(999L)).thenReturn(Boolean.TRUE);
		when(articleRepository.existsByUserId(999L)).thenReturn(Boolean.FALSE);

		assertThatThrownBy(() -> articleService.deleteByUserId(999L)).isInstanceOf(ArticleNotFoundException.class);
	}

}
