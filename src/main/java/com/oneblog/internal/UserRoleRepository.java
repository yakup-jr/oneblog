package com.oneblog.internal;

public interface UserRoleRepository {

	void save(Long userId, Long roleId);

	void deleteByUserId(Long userId);

}
