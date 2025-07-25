package net.oneblog.article;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {"net.oneblog.article", "net.oneblog.user", "net.oneblog" +
    ".sharedexceptions", "net.oneblog.sharedconfig", "net.oneblog.api"})
@EntityScan(basePackages = {"net.oneblog.article", "net.oneblog.user", "net.oneblog" +
    ".sharedexceptions", "net.oneblog.sharedconfig", "net.oneblog.api"})
@EnableJpaRepositories(
    basePackages = {"net.oneblog.article", "net.oneblog.user", "net.oneblog" +
        ".sharedexceptions", "net.oneblog.sharedconfig", "net.oneblog.api"})
@SpringBootApplication
public class TestArticleApplication {
}
