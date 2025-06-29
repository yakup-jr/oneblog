package com.oneblog.article;

import com.oneblog.article.dto.ArticleDto;
import com.oneblog.user.UserLink;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * The type Article model assembler.
 */
@Component
public class ArticleModelAssembler implements RepresentationModelAssembler<ArticleDto, EntityModel<ArticleDto>> {

	private final ArticleLink articleLink;

	private final UserLink userLink;

	/**
	 * Instantiates a new Article model assembler.
	 *
	 * @param articleLink the article link
	 * @param userLink    the user link
	 */
	public ArticleModelAssembler(ArticleLink articleLink, UserLink userLink) {
		this.articleLink = articleLink;
		this.userLink = userLink;
	}

	@Override
	@NonNull
	public EntityModel<ArticleDto> toModel(@NonNull ArticleDto articleDto) {
		return EntityModel.of(articleDto, articleLink.findArticleByArticleId(articleDto.getArticleId()).withSelfRel(),
		                      userLink.findUserByArticleId(articleDto.getUser().getUserId()).withRel("user"));
	}
}

