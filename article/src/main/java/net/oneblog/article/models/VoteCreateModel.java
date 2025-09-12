package net.oneblog.article.models;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.oneblog.api.interfaces.VoteType;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "votes")
public class VoteCreateModel {

    @NotNull
    private Long articleId;

    @NotNull
    private Long userId;

    @NotNull
    private VoteType voteType;

}
