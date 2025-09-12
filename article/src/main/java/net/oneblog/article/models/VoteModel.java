package net.oneblog.article.models;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.oneblog.api.interfaces.VoteType;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "votes")
public class VoteModel {

    @NotNull
    @Min(value = 1L, message = "must be greater than or equal to 1")
    private Long voteId;

    @NotNull
    @Valid
    private ArticleModel article;

    @NotNull
    @Valid
    private ValidatedUserModel user;

    @NotNull
    private VoteType voteType;
}
