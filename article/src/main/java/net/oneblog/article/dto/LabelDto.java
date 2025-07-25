package net.oneblog.article.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.oneblog.api.interfaces.LabelName;
import net.oneblog.article.controller.LabelController;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type Label dto.
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "labels")
public class LabelDto {

    @NotNull
    @Min(value = 1L, message = "must be greater than or equal to 1")
    private Long labelId;

    @NotNull(groups = {LabelController.class})
    private LabelName name;

}
