package com.bearcode.patches;

import com.bearcode.patches.V75.Instructions;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import com.googlecode.flyway.core.migration.java.JavaMigrationChecksumProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Date: 12.08.14
 * Time: 16:43
 *
 * @author Leonid Ginzburg
 */
public class V75__add_indiana_instructions implements JavaMigration, JavaMigrationChecksumProvider {
	// Update the CHECK_SUM if you make any changes into the database schema
	private static final Integer CHECK_SUM = 0x01012012;

	private static final Log log = LogFactory.getLog(V75__add_indiana_instructions.class);

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
        addInstructions();

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

    private void addInstructions() {
        for (Instructions instructions : Instructions.values() ) {
            if ( checkInstructions( instructions ) ) {
                createInstructions( instructions );
            }
        }
    }

    /**
     *
     * @param instructions instruction description
     * @return true if instructions do not present
     */
    private boolean checkInstructions(Instructions instructions) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * FROM pdf_fillings WHERE in_pdf_name = ?", instructions.getInPdfName());
        return !rowSet.next();
    }

    private void createInstructions(Instructions instructions) {
        final String insertRelated = "INSERT INTO dependents VALUES()";
        final String insertPdfFillings = "INSERT INTO pdf_fillings (id, `name`, in_pdf_name, text) VALUES(?,?,?,?)";
        final String insertDependency = "INSERT INTO question_dependencies (kind,variant_id,field_name,field_value) VALUES('USER',?,'VotingState','IN')";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement( Connection connection ) throws SQLException {
                        return connection.prepareStatement( insertRelated, new String[]{"id"} );
                    }
                },
                keyHolder );
        long id = keyHolder.getKey().longValue();
        jdbcTemplate.update( insertPdfFillings, id, instructions.getDescription(), instructions.getInPdfName(), instructions.getText());
        jdbcTemplate.update( insertDependency, id );

    }
}
