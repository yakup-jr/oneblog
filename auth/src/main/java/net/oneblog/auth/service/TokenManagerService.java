package net.oneblog.auth.service;

import net.oneblog.validationapi.models.ValidatedUserModel;

public interface TokenManagerService {

    void revokeAllTokensForUser(ValidatedUserModel userModel);

    void saveUserToken(String accessToken, String refreshToken, ValidatedUserModel userModel);

}
