package net.oneblog.auth.models;

import lombok.*;
import net.oneblog.validationapi.models.ValidatedUserModel;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoogleRegistrationRequestModel {

    private String googleUserId;

    private ValidatedUserModel userModel;

}
