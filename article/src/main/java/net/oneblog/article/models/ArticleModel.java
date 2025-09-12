package net.oneblog.article.models;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

/**
 * The type Article dto.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Relation(collectionRelation = "articles")
public class ArticleModel {

    @NotNull
    @Min(value = 1L)
    private Long articleId;

    @NotNull
    @Length(min = 1, max = 255)
    private String title;

    @NotNull
    @Length(min = 10, max = 65000)
    private String body;

    @NotNull
    @Min(0)
    private Long likes;

    @NotNull
    @Min(0)
    private Long dislikes;

    @Valid
    private Set<VoteModel> votes;

    @NotNull
    private LocalDateTime createdAt;

    private String previewBody;

    @NotNull
    private List<LabelModel> labels;

    @NotNull
    private ValidatedUserModel user;
}
