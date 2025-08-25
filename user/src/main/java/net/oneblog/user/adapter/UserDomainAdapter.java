package net.oneblog.user.adapter;

import net.oneblog.api.interfaces.UserDomain;
import net.oneblog.user.entity.UserEntity;

/**
 * The type User domain adapter.
 */
public class UserDomainAdapter implements UserDomain {

    private final UserEntity entity;

    /**
     * Instantiates a new User domain adapter.
     *
     * @param entity the entity
     */
    public UserDomainAdapter(UserEntity entity) {
        this.entity = entity;
    }


    @Override
    public Long getUserId() {
        return entity.getUserId();
    }

    @Override
    public String getName() {
        return entity.getName();
    }

    @Override
    public String getNickname() {
        return entity.getNickname();
    }

    @Override
    public String getEmail() {
        return entity.getEmail();
    }
}
