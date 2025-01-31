package com.oneblog.user;

import com.oneblog.article.Article;
import com.oneblog.article.label.Label;
import com.oneblog.article.label.LabelName;
import com.oneblog.article.preview.Preview;
import com.oneblog.exceptions.ApiRequestException;
import com.oneblog.user.role.Role;
import com.oneblog.user.role.RoleName;
import com.oneblog.user.role.RoleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private RoleService roleService;

	@InjectMocks
	private UserServiceImpl userService;

	private static final User defaultUser = new User(1L, "Dima", "yakup_jr", "somemail@mail.com", "strongPass",
	                                                 List.of(new Role(1L, RoleName.ROLE_USER, null)), null,
	                                                 null, List.of(
		new Article(1L, "The awesome title", "The awesome body", null, new Preview(1L, "preview body", null),
		            List.of(new Label(1L, LabelName.Java, null)), null)));

	@Test
	void save_ReturnUser() {
		User requestUser = new User(null, defaultUser.getName(), defaultUser.getNickname(), defaultUser.getEmail(),
		                            defaultUser.getPassword(), defaultUser.getRoles(), null, null, null);

		User responseUser =
			new User(defaultUser.getUserId(), defaultUser.getName(), defaultUser.getNickname(), defaultUser.getEmail(),
			         defaultUser.getPassword(), defaultUser.getRoles(), null, null, null);

		when(userRepository.existsByNickname(requestUser.getNickname())).thenReturn(false);
		when(userRepository.existsByEmail(requestUser.getEmail())).thenReturn(false);
		when(userRepository.save(requestUser)).thenReturn(responseUser);
		when(roleService.findByName(requestUser.getRoles().getFirst().getName().toString())).thenReturn(
			defaultUser.getRoles().getFirst());

		User savedUser = userService.save(requestUser);

		assertThat(savedUser).isNotNull().isInstanceOf(User.class);
		assertThat(savedUser.getUserId()).isEqualTo(responseUser.getUserId());
		assertThat(savedUser).isEqualTo(responseUser);
	}

	@Test
	void save_ThrowApiRequestException_NicknameExists() {
		User requestUser = new User(null, defaultUser.getName(), defaultUser.getNickname(), defaultUser.getEmail(),
		                            defaultUser.getPassword(), defaultUser.getRoles(), null, null, null);
		when(userRepository.existsByNickname(requestUser.getNickname())).thenReturn(true);

		assertThatThrownBy(() -> userService.save(requestUser)).isInstanceOf(ApiRequestException.class);
	}

	@Test
	void save_ThrowApiRequestException_EmailExists() {
		User requestUser = new User(null, defaultUser.getName(), defaultUser.getNickname(), defaultUser.getEmail(),
		                            defaultUser.getPassword(), defaultUser.getRoles(), null, null, null);
		when(userRepository.existsByNickname(requestUser.getNickname())).thenReturn(false);
		when(userRepository.existsByEmail(requestUser.getEmail())).thenReturn(true);

		assertThatThrownBy(() -> userService.save(requestUser)).isInstanceOf(ApiRequestException.class);
	}

	@Test
	void existsById_ReturnTrue() {
		when(userRepository.existsById(1L)).thenReturn(true);

		boolean userExists = userService.existsById(1L);

		assertThat(userExists).isTrue();
	}

	@Test
	void existsById_ReturnFalse() {
		when(userRepository.existsById(999L)).thenReturn(false);

		boolean userExists = userService.existsById(999L);

		assertThat(userExists).isFalse();
	}

	@Test
	void findById_ReturnUser() {
		when(userRepository.findById(1L)).thenReturn(Optional.of(defaultUser));

		User responseUser = userService.findById(1L);

		assertThat(responseUser).isNotNull().isInstanceOf(User.class);
		assertThat(responseUser.getUserId()).isEqualTo(1L);
		assertThat(responseUser).isEqualTo(defaultUser);
	}

	@Test
	void findById_ThrowUserNotFoundException() {
		when(userRepository.findById(999L)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> userService.findById(999L)).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	void findByNickname_ReturnUser() {
		when(userRepository.findByNickname(defaultUser.getNickname())).thenReturn(Optional.of(defaultUser));

		User responseUser = userService.findByNickname(defaultUser.getNickname());

		assertThat(responseUser).isNotNull().isInstanceOf(User.class);
		assertThat(responseUser.getUserId()).isEqualTo(defaultUser.getUserId());
		assertThat(responseUser).isEqualTo(defaultUser);
	}

	@Test
	void findByNickname_ThrowUserNotFoundException() {
		when(userRepository.findByNickname(defaultUser.getNickname())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> userService.findByNickname(defaultUser.getNickname())).isInstanceOf(
			UserNotFoundException.class);
	}

	@Test
	void findByEmail_ReturnUser() {
		when(userRepository.findByEmail(defaultUser.getEmail())).thenReturn(Optional.of(defaultUser));

		User responseUser = userService.findByEmail(defaultUser.getEmail());

		assertThat(responseUser).isNotNull().isInstanceOf(User.class);
		assertThat(responseUser.getUserId()).isEqualTo(defaultUser.getUserId());
		assertThat(responseUser).isEqualTo(defaultUser);
	}

	@Test
	void findByEmail_ThrowUserNotFoundException() {
		when(userRepository.findByEmail(defaultUser.getEmail())).thenReturn(Optional.empty());

		assertThatThrownBy(() -> userService.findByEmail(defaultUser.getEmail())).isInstanceOf(
			UserNotFoundException.class);
	}

	@Test
	void deleteById_ReturnUser() {
		when(userRepository.existsById(1L)).thenReturn(true);

		userService.deleteById(1L);
	}

	@Test
	void deleteById_ThrowUserNotFoundException() {
		when(userRepository.existsById(999L)).thenReturn(false);

		assertThatThrownBy(() -> userService.deleteById(999L)).isInstanceOf(UserNotFoundException.class);
	}
}
