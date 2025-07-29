package net.oneblog.auth;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"net.oneblog.sharedconfig", "net.oneblog.user", "net.oneblog" +
    ".sharedexceptions", "net.oneblog.auth", "net.oneblog.email", "net.oneblog.validationapi"})
@EntityScan(basePackages = {"net.oneblog.sharedconfig", "net.oneblog.user", "net.oneblog" +
    ".sharedexceptions", "net.oneblog.auth", "net.oneblog.email", "net.oneblog.validationapi"})
@EnableJpaRepositories(
    basePackages = {"net.oneblog.sharedconfig", "net.oneblog.user", "net.oneblog" +
        ".sharedexceptions", "net.oneblog.auth", "net.oneblog.email", "net.oneblog.validationapi"})
public class TestAuthApplication {
}
