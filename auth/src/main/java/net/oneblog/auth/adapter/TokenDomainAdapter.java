package net.oneblog.auth.adapter;

import net.oneblog.api.interfaces.TokenDomain;
import net.oneblog.auth.entity.TokenEntity;

public class TokenDomainAdapter implements TokenDomain {
    private final TokenEntity entity;

    public TokenDomainAdapter(TokenEntity entity) {
        this.entity = entity;
    }

    @Override
    public Long getTokenId() {
        return entity.getTokenId();
    }

    @Override
    public String getAccessToken() {
        return entity.getAccessToken();
    }

    @Override
    public String getRefreshToken() {
        return entity.getRefreshToken();
    }

    @Override
    public Boolean isRevoked() {
        return entity.getIsRevoke();
    }
}
