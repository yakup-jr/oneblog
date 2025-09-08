package net.oneblog.article.links;

import net.oneblog.article.models.LabelModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * The type Label model assembler.
 */
@Component
public class LabelModelAssembler
    implements RepresentationModelAssembler<LabelModel, EntityModel<LabelModel>> {


    private final LabelLink labelLink;

    /**
     * Instantiates a new Label model assembler.
     *
     * @param labelLink the label link
     */
    public LabelModelAssembler(LabelLink labelLink) {
        this.labelLink = labelLink;
    }


    @Override
    @NonNull
    public EntityModel<LabelModel> toModel(@NonNull LabelModel labelModel) {
        return EntityModel.of(labelModel,
            labelLink.findLabelByLabelId(labelModel.getLabelId()).withSelfRel());
    }
}
