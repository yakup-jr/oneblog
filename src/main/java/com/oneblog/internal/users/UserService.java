package com.oneblog.internal.users;

public interface UserService {

	User createUser(User user);

	User getUser(String username);

	void deleteUser(Long id);

}
