package net.oneblog.article.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.oneblog.api.interfaces.LabelName;
import org.springframework.hateoas.server.core.Relation;

/**
 * The type Label create dto.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "labels")
public class LabelCreateModel {

    @NotNull
    private LabelName name;

}
