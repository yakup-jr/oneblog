package com.oneblog.internal.users;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

	Optional<User> findByNickname(String nickname);

	List<User> findAll();

	User save(User user);

	void deleteById(Long id);

}
