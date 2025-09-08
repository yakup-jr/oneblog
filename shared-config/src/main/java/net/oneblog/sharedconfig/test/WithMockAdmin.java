package net.oneblog.sharedconfig.test;


import org.springframework.security.test.context.support.WithMockUser;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The interface With mock admin.
 */
@Retention(RetentionPolicy.RUNTIME)
@WithMockUser(value = "admin", roles = {"USER", "ADMIN"})
public @interface WithMockAdmin {
}
