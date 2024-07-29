package com.oneblog.internal.roles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class JdbcRoleRepository implements RoleRepository {

	private final JdbcTemplate jdbcTemplate;

	public JdbcRoleRepository(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public Role save(RoleName roleName) {
		String sql = "insert into T_ROLE (NAME) values (?)";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps =
					connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			ps.setString(1, roleName.name());
			return ps;
		}, keyHolder);

		return new Role(keyHolder.getKey().longValue(), roleName);
	}

	@Override
	public List<Role> findAll() {
		String sql = "select * from T_ROLE";

		return jdbcTemplate.query(sql, this::mapRoles);
	}

	@Override
	public List<Role> findByUserId(Long userId) {
		String sql = "select r.ID as ID, r.NAME as NAME from T_ROLE r, T_USER_ROLE ur " +
		             "where ur.USER_ID = ? and ur.ROLE_ID = r.ID";

		return jdbcTemplate.query(sql, this::mapRoles, userId);
	}

	@Override
	public Optional<Role> findByName(RoleName roleName) {
		String sql = "select r.ID as ID, r.NAME as NAME from T_ROLE r where r.NAME = ?";

		return Optional.ofNullable(jdbcTemplate.query(sql, this::mapRole, roleName.toString()));
	}

	@Override
	public Optional<Role> findById(Long id) {
		String sql = "select r.ID as ID, r.NAME as NAME from T_ROLE r where r.ID = ?";

		return Optional.ofNullable(jdbcTemplate.query(sql, this::mapRole, id));
	}

	@Override
	public void deleteByUserId(Long userId) {
		String sql = "delete from T_ROLE where ID = (select ur.ROLE_ID from T_USER_ROLE ur where " +
		             "ur.USER_ID = ?)";

		jdbcTemplate.update(sql, userId);
	}

	private Role mapRole(ResultSet resultSet) throws SQLException {
		Role role = null;
		while (resultSet.next()) {
			if (role == null) {
				Long id = resultSet.getLong("ID");
				String name = resultSet.getString("NAME");
				role = new Role(id, RoleName.valueOf(name));
			}
		}
		if (role == null) {
			throw new EmptyResultDataAccessException(1);
		}
		return role;
	}

	private List<Role> mapRoles(ResultSet resultSet) throws SQLException {
		ArrayList<Role> roles = new ArrayList<>();
		while (resultSet.next()) {
			Long id = resultSet.getLong("ID");
			String name = resultSet.getString("NAME");
			roles.add(new Role(id, RoleName.valueOf(name)));
		}
		if (roles.isEmpty()) {
			throw new EmptyResultDataAccessException(1);
		}
		return roles.stream().toList();
	}
}
