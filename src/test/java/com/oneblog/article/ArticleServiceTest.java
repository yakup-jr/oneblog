package com.oneblog.article;

import com.oneblog.article.label.Label;
import com.oneblog.article.label.LabelName;
import com.oneblog.article.preview.Preview;
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
