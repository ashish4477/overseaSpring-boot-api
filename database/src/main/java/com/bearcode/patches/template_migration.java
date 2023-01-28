package com.youdiligence.db.migration.v4;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.StringUtils;

import com.googlecode.flyway.core.migration.java.JavaMigration;
import com.googlecode.flyway.core.migration.java.JavaMigrationChecksumProvider;

/**
 * This template for java based patch
 * Rename this file to VX__name_of_patch.java
 *
 */
public class template_migration implements JavaMigration, JavaMigrationChecksumProvider {
	// Update the CHECK_SUM if you make any changes into the database schema
	private static final Integer CHECK_SUM = 0x01012012;

	private static final Log log = LogFactory.getLog(template_migration.class);

	private JdbcTemplate jdbcTemplate;
	
	@Override
	public Integer getChecksum() {
		return CHECK_SUM;
	}

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		this.jdbcTemplate = jdbcTemplate;

		log.info("- Start migration...");

		disableForeignKeyChecks();

		// You migration code here...

		enableForeignKeyChecks();
	}

	private void enableForeignKeyChecks() throws SQLException {
		this.jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=1");
		this.jdbcTemplate.execute("SET UNIQUE_CHECKS=1");
	}

	private void disableForeignKeyChecks() throws SQLException {
		this.jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS=0");
		this.jdbcTemplate.execute("SET UNIQUE_CHECKS=0");
	}
}
