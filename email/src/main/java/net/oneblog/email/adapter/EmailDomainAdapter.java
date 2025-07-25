package net.oneblog.email.adapter;

import net.oneblog.api.interfaces.EmailDomain;
import net.oneblog.api.interfaces.UserDomain;
import net.oneblog.email.entity.EmailEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class EmailDomainAdapter implements EmailDomain {
    private final EmailEntity entity;

    public EmailDomainAdapter(EmailEntity entity) {
        this.entity = entity;
    }

    @Override
    public Long getEmailId() {
        return entity.getEmailId();
    }

    @Override
    public String getCode() {
        return entity.getCode();
    }

    @Override
    public LocalDateTime getExpiredAt() {
        return entity.getExpiresAt();
    }

    @Override
    public UserDomain getUser() {
        return entity.getUserEntity();
    }
}
