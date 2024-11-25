package com.oneblog.article;

import com.oneblog.article.dto.ArticleDto;
import com.oneblog.user.UserLink;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ArticleModelAssembler implements RepresentationModelAssembler<ArticleDto, EntityModel<ArticleDto>> {

	private final ArticleLink articleLink;

	private final UserLink userLink;

	public ArticleModelAssembler(ArticleLink articleLink, UserLink userLink) {
		this.articleLink = articleLink;
		this.userLink = userLink;
	}

	@Override
	public EntityModel<ArticleDto> toModel(ArticleDto articleDto) {
		return EntityModel.of(articleDto, articleLink.findArticleByArticleId(articleDto.getArticleId()).withSelfRel(),
		                      userLink.findUserByArticleId(articleDto.getUser().getUserId()).withRel("user"));
	}
}

