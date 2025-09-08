package net.oneblog.article.models;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

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
    private LocalDateTime createdAt;

    @NotNull
    private PreviewModel preview;

    @NotNull
    private List<LabelModel> labels;

    @NotNull
    private ValidatedUserModel user;
}
