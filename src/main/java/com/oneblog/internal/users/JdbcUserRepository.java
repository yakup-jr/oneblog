package com.oneblog.internal.users;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
public class JdbcUserRepository implements UserRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcUserRepository(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<User> findAll() {
		String sql = "select * from T_USER";

		return jdbcTemplate.query(sql, this::mapUsers);
	}

	@Override
	public Optional<User> findByNickname(String nickname) {
		String sql = "select u.ID as ID, u.NICKNAME as NICKNAME, u.NAME as NAME, u.EMAIL as " +
		             "EMAIL, u.PASSWORD as PASSWORD, r.NAME as ROLE from T_USER u, " +
		             "T_USER_ROLE ur, T_ROLE r where u.NICKNAME = ? and ur.USER_ID " +
		             "= u.ID and ur.ROLE_ID = r.ID";

		return Optional.ofNullable(jdbcTemplate.query(sql, this::mapUser, nickname));

	}

	@Override
	public User save(User user) {
		String sql =
				"insert into T_USER (NICKNAME, NAME, EMAIL, PASSWORD) values (?, ?, ?, ?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps =
					connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, user.getNickname());
			ps.setString(2, user.getName());
			ps.setString(3, user.getEmail());
			ps.setString(4, user.getPassword());
			return ps;
		}, keyHolder);

		return User.builder().id(keyHolder.getKey().longValue()).nickname(user.getNickname()).name(user.getName())
		           .email(user.getEmail()).password(user.getPassword()).build();
	}

	@Override
	public void deleteById(Long id) {
		String sql = "delete from T_USER where ID = ?";

		jdbcTemplate.update(sql, id);
	}

	public User mapUser(ResultSet rs) throws SQLException {
		User user = null;
		while (rs.next()) {
			if (user == null) {
				String nickname = rs.getString("NICKNAME");
				String name = rs.getString("NAME");
				String email = rs.getString("EMAIL");
				String password = rs.getString("PASSWORD");
				Long id = rs.getLong("ID");
				user = User.builder().nickname(nickname).name(name).email(email).password(password)
				           .id(id).build();
			}
		}
		if (user == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return user;
	}

	public List<User> mapUsers(ResultSet rs) throws SQLException {
		ArrayList<User> users = new ArrayList<>();
		while (rs.next()) {
			Long id = rs.getLong("ID");
			String nickname = rs.getString("NICKNAME");
			String name = rs.getString("NAME");
			String email = rs.getString("EMAIL");
			String password = rs.getString("PASSWORD");
			users.add(new User(id, name, nickname, email, password, null));
		}
		if (users.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return users.stream().toList();
	}
}
