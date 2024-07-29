package com.oneblog.internal;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcUserRoleRepository implements UserRoleRepository {

	private JdbcTemplate jdbcTemplate;

	public JdbcUserRoleRepository(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public void save(Long userId, Long roleId) {
		String sql = "insert into T_USER_ROLE (USER_ID, ROLE_ID) values (?, ?)";

		jdbcTemplate.update(sql, userId, roleId);
	}

	@Override
	public void deleteByUserId(Long userId) {
		String sql = "delete from T_USER_ROLE where USER_ID = ?";

		jdbcTemplate.update(sql, userId);
	}
}
