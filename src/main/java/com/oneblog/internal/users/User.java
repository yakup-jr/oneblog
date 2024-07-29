package com.oneblog.internal.users;

import com.oneblog.internal.roles.Role;
import com.oneblog.internal.roles.RoleName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

	private Long id;

	private String name;

	private String nickname;

	private String email;

	private String password;

	private List<Role> roles;
}
