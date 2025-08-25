package net.oneblog.article;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"net.oneblog.article", "net.oneblog.user", "net.oneblog" +
    ".sharedexceptions", "net.oneblog.sharedconfig", "net.oneblog.api",
    "net.oneblog.validationapi"})
@EntityScan(basePackages = {"net.oneblog.article", "net.oneblog.user", "net.oneblog" +
    ".sharedexceptions", "net.oneblog.sharedconfig", "net.oneblog.api",
    "net.oneblog.validationapi"})
@EnableJpaRepositories(
    basePackages = {"net.oneblog.article", "net.oneblog.user", "net.oneblog" +
        ".sharedexceptions", "net.oneblog.sharedconfig", "net.oneblog.api",
        "net.oneblog.validationapi"})
@SpringBootApplication
public class TestArticleApplication {
}
