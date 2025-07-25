package net.oneblog.api.interfaces;

public interface TokenDomain {
    Long getTokenId();

    String getAccessToken();

    String getRefreshToken();

    Boolean isRevoked();
}
