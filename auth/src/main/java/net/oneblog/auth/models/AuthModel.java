package net.oneblog.auth.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.hateoas.server.core.Relation;

import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Relation(collectionRelation = "auths")
public class AuthModel {

    @NotNull
    private Long authId;

    @NotNull
    @Valid
    private boolean verificated;

    private String googleUserId;

    @NotNull
    @Valid
    private List<RoleModel> roleModels;

    @Valid
    private ValidatedUserModel userDto;

}
