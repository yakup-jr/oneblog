package net.oneblog.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"net.oneblog.sharedconfig", "net.oneblog.user", "net.oneblog" +
    ".sharedexceptions", "net.oneblog.auth", "net.oneblog.email"})
@EntityScan(basePackages = {"net.oneblog.sharedconfig", "net.oneblog.user", "net.oneblog" +
    ".sharedexceptions", "net.oneblog.auth", "net.oneblog.email"})
@EnableJpaRepositories(
    basePackages = {"net.oneblog.sharedconfig", "net.oneblog.user", "net.oneblog" +
        ".sharedexceptions", "net.oneblog.auth", "net.oneblog.email"})
public class TestAuthApplication {
}
