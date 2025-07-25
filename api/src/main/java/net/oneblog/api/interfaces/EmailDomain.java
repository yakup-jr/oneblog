package net.oneblog.api.interfaces;

import java.time.LocalDateTime;

public interface EmailDomain {
    Long getEmailId();
    String getCode();
    LocalDateTime getExpiredAt();
    UserDomain getUser();
}
