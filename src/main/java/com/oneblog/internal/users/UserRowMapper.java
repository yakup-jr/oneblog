package com.oneblog.internal.users;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setId(rs.getLong("id"));
		user.setName(rs.getString("name"));
		user.setNickname(rs.getString("nickname"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		return user;
	}
}
