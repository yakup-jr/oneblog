package net.oneblog.article.links;

import lombok.AllArgsConstructor;
import net.oneblog.article.models.VoteModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VoteModelAssembler
    implements RepresentationModelAssembler<VoteModel, EntityModel<VoteModel>> {

    private final VoteLink voteLink;

    @NonNull
    @Override
    public EntityModel<VoteModel> toModel(@NonNull VoteModel entity) {
        return EntityModel.of(entity,
            voteLink.findVoteByVoteId(entity.getVoteId()).withRel("vote by vote_id")
            , voteLink.findVotesByArticleId(entity.getArticle().getArticleId()).withRel("votes by" +
                " article_id"), voteLink.findVotesByUserId(entity.getUser().userId()).withRel(
                "votes by user_id"));
    }

    @NonNull
    @Override
    public CollectionModel<EntityModel<VoteModel>> toCollectionModel(
        @NonNull Iterable<? extends VoteModel> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}
