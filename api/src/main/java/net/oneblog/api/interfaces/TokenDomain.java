package net.oneblog.api.interfaces;

import java.time.LocalDateTime;

public interface TokenDomain {
	Long getTokenId();

	String getAccessToken();

	String getRefreshToken();

	Boolean isRevoked();
}
