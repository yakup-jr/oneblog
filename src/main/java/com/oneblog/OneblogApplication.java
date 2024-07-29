package com.oneblog;

import com.oneblog.internal.roles.Role;
import com.oneblog.internal.roles.RoleName;
import com.oneblog.internal.roles.RoleRepository;
import com.oneblog.internal.users.User;
import com.oneblog.internal.users.UserRepository;
import com.oneblog.internal.users.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.List;

@Log4j2
@SpringBootApplication
public class OneblogApplication {

	private UserService userService;

	public OneblogApplication(UserService userService) {
		this.userService = userService;
	}

	public static void main(String[] args) {
		new SpringApplicationBuilder().profiles("dev").sources(OneblogApplication.class).run(args);

	}

	@Bean
	ApplicationRunner applicationRunner(Environment env, RoleRepository roleRepository,
	                                    UserRepository userRepository) {
		return args -> {
			log.info(env.getProperty("message-from-application-properties"));
			userService.createUser(
					User.builder().email("123@gmail.com").name("Dima").nickname("somenick")
					    .password("4326487164").roles(List.of(new Role(RoleName.ROLE_ADMIN)))
					    .build());
			log.info(userService.getUser("WHTOY"));
			log.info(userService.getUser("somenick"));
			userService.deleteUser(2L);
			log.info(userRepository.findAll());
		};
	}
}
