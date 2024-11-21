package com.oneblog.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByNickname(String nickname);

	Optional<User> findByEmail(String email);

	boolean existsByEmailOrNickname(String email, String nickname);

}
