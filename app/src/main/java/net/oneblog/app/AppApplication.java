package net.oneblog.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type App application.
 */
@SpringBootApplication(scanBasePackages = "net.oneblog")
@EnableJpaRepositories(basePackages = "net.oneblog")
@EntityScan(basePackages = "net.oneblog")
public class AppApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

}
