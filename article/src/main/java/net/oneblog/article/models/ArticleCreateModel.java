package net.oneblog.article.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

/**
 * The type Article creates dto.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "articles")
public class ArticleCreateModel {
    @NotBlank
    @Length(min = 1, max = 255)
    private String title;

    @NotBlank
    @Length(min = 10, max = 65000)
    private String body;

    @NotNull
    @Valid
    private PreviewCreateModel preview;

    @NotNull
    @Valid
    private List<LabelModel> labels;

    @NotNull
    @Valid
    private ValidatedUserModel user;

}
