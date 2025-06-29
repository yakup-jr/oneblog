package com.oneblog.user;

import com.oneblog.article.ArticleLink;
import com.oneblog.user.dto.UserDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

/**
 * The type User model assembler.
 */
@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

	private final ArticleLink articleLink;

	private final UserLink userLink;

	/**
	 * Instantiates a new User model assembler.
	 *
	 * @param articleLink the article link
	 * @param userLink    the user link
	 */
	public UserModelAssembler(ArticleLink articleLink, UserLink userLink) {
		this.articleLink = articleLink;
		this.userLink = userLink;
	}

	@Override
	public EntityModel<UserDto> toModel(UserDto userDto) {
		return EntityModel.of(userDto, userLink.findUserByUserId(userDto.getUserId()).withSelfRel(),
		                      articleLink.findArticleByUserId(userDto.getUserId()).withRel("articles"));
	}
}
