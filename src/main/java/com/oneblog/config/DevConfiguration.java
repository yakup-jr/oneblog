package com.oneblog.config;

import com.oneblog.internal.JdbcUserRoleRepository;
import com.oneblog.internal.UserRoleRepository;
import com.oneblog.internal.roles.JdbcRoleRepository;
import com.oneblog.internal.roles.RoleRepository;
import com.oneblog.internal.users.JdbcUserRepository;
import com.oneblog.internal.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;

@Slf4j
@Configuration
@Profile("dev")
public class DevConfiguration {

	@Bean
	public DataSource dataSource() {
		return (new EmbeddedDatabaseBuilder().addScript("classpath:/static/devdb/schema.sql")
		                                     .addScript("classpath:/static/devdb/data.sql")
		                                     .build());
	}

	@Bean
	public UserRepository userRepository() {
		return new JdbcUserRepository(dataSource());
	}

	@Bean
	public RoleRepository roleRepository() {
		return new JdbcRoleRepository(dataSource());
	}

	@Bean
	public UserRoleRepository userRoleRepository() {
		return new JdbcUserRoleRepository(dataSource());
	}

}
