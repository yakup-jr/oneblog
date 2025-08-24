package net.oneblog.auth.service;


import net.oneblog.auth.models.AuthModel;
import net.oneblog.auth.models.BasicRegistrationRequestModel;
import net.oneblog.auth.models.GoogleRegistrationRequestModel;

public interface AuthService {

    AuthModel save(GoogleRegistrationRequestModel googleRegistrationModel);

    AuthModel save(BasicRegistrationRequestModel basicRegistrationModel);

    AuthModel findByEmail(String email);

    AuthModel findByGoogleUserId(String googleUserId);

    void update(AuthModel authModel);

}
