package com.oneblog.user;

import com.oneblog.exceptions.ApiRequestException;

public interface UserService {

	User save(User user) throws ApiRequestException;

	boolean existsById(Long userId);

	User findById(Long id) throws UserNotFoundException;

	User findByNickname(String nickname) throws UserNotFoundException;

	User findByEmail(String email) throws UserNotFoundException;

	User deleteById(Long id) throws UserNotFoundException;

}
