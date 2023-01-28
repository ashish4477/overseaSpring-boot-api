package com.bearcode.patches;

import com.googlecode.flyway.core.migration.java.JavaMigration;
import com.googlecode.flyway.core.migration.java.JavaMigrationChecksumProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 23.06.14
 * Time: 20:28
 *
 * @author Leonid Ginzburg
 */
public class V72__clean_up_leo_general_email implements JavaMigration, JavaMigrationChecksumProvider {
	// Update the CHECK_SUM if you make any changes into the database schema
	private static final Integer CHECK_SUM = 0x01012012;

	private static final Log log = LogFactory.getLog(V72__clean_up_leo_general_email.class);

    private static final String USERNAME_PATTERN = "[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA_Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?";

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

        Pattern emailPattern = Pattern.compile(".*[^\\.a-zA-Z0-9!#$%&'*+/=?^_`{|}~-](" + USERNAME_PATTERN + ").*");
        // You migration code here...
        SqlRowSet result = this.jdbcTemplate.queryForRowSet("SELECT id, general_email FROM local_officials");
        while ( result.next() ) {
            int id = result.getInt("id");
            String email = result.getString("general_email");

            if ( !email.isEmpty() && !email.matches(USERNAME_PATTERN) ) {
                Matcher matcher = emailPattern.matcher( email );
                String newEmail = "";
                if ( matcher.matches() ) {
                    newEmail = matcher.group(1);
                    if ( newEmail == null ) newEmail = "";
                }
                this.jdbcTemplate.update("UPDATE local_officials SET general_email = ? WHERE id = ?", newEmail, id );
            }
        }
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

