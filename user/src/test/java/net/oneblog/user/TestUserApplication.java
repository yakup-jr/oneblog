package net.oneblog.user;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
    basePackages = {"net.oneblog.sharedexceptions", "net.oneblog.sharedconfig", "net.oneblog" +
        ".user", "net.oneblog.validationapi"})
public class TestUserApplication {
}
