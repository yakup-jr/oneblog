package net.oneblog.article.links;

import net.oneblog.article.controller.VoteController;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class VoteLink {

    private static final Class<VoteController> controllerClass = VoteController.class;

    public WebMvcLinkBuilder findVoteByVoteId(Long voteId) {
        return linkTo(methodOn(controllerClass).findVoteByVoteId(voteId));
    }

    public WebMvcLinkBuilder findVotesByArticleId(Long articleId) {
        return linkTo(methodOn(controllerClass).findVotesByArticleId(articleId));
    }

    public WebMvcLinkBuilder findVotesByUserId(Long userId) {
        return linkTo(methodOn(controllerClass).findVotesByUserId(userId));
    }
}
