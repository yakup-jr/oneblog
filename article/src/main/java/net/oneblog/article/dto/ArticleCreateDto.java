package net.oneblog.article.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.oneblog.user.dto.UserDto;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The type Article creates dto.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "articles")
public class ArticleCreateDto {

    @NotBlank
    @Length(min = 1, max = 255)
    private String title;

    @NotBlank
    @Length(min = 10, max = 65000)
    private String body;

    private LocalDateTime createTime;

    @NotNull
    @Valid
    private PreviewCreateDto preview;

    @NotNull
    @Valid
    private List<LabelDto> labels;

    @NotNull
    @Valid
    private UserDto user;

}
