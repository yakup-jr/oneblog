package com.oneblog.helpers;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

public class DatabaseCleanerExtension implements AfterEachCallback {

	private JdbcTemplate jdbcTemplate;

	@Override
	public void afterEach(ExtensionContext extensionContext) throws Exception {
		if (jdbcTemplate == null) {
			jdbcTemplate = SpringExtension.getApplicationContext(extensionContext).getBean(JdbcTemplate.class);
		}
		cleanDatabase();
	}

	public void cleanDatabase() {
		List<String> tableNames =
			jdbcTemplate.queryForList("SELECT table_name FROM information_schema.tables WHERE table_schema='public'",
			                          String.class);
		for (String tableName : tableNames) {
			jdbcTemplate.execute("TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE");
		}
	}
}
