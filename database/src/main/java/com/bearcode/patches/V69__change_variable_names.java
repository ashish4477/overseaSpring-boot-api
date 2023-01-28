package com.bearcode.patches;

import com.googlecode.flyway.core.migration.java.JavaMigration;
import com.googlecode.flyway.core.migration.java.JavaMigrationChecksumProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.SQLException;

/**
 * Date: 12.11.13
 * Time: 22:49
 *
 * @author Leonid Ginzburg
 */
public class V69__change_variable_names implements JavaMigration, JavaMigrationChecksumProvider {
	// Update the CHECK_SUM if you make any changes into the database schema
	private static final Integer CHECK_SUM = 0x01012012;

	private static final Log log = LogFactory.getLog(V69__change_variable_names.class);

	private JdbcTemplate jdbcTemplate;

    private static final String oldNames[] = {
            "1_4_c_1",
            "1_5_a_1",
            "1_6_a_1",
            "1_7_a_1",
            "2_1_a_1",
            "1_2_f_1",
            "1_2_g_1",
            "1_2_i_1",
            "3_1_a_1",

            "ssn_4",
            "for_how_long",
            "drivers_license",
            "AS_age",
            "employer_name",
            "AS_attest",
            "ssn_9",
            "AZ_drivers",
            "VA_available",
            "optional_passport_will_not",
            "optional_military_id_will_not",
            "optional_military_id_will",
            "previous_county",
            "AZ_nonop",
            "AZ_indian",
            "AZ_treaty",
            "AZ_tribal",
            "AZ_naturalization",
            "VA_date",
            "additional_notes",
            "fax_from",
            "fax_till",
            "employer_address1",
            "employer_address2",
            "employer_city",
            "employer_postalCode",
            "employer_country",
            "military_branch",
            "military_rank",
            "no_req_ssn_will",
            "no_req_passport_will",
            "passport_number",
            "additional_id_passport",
            "additional_id_state_id",
            "rava_intent_new_addr",
            "rava_intent_ballot",
            "fax_forward",
            "rava_intent_new_reg",
            "rava_intent_new_party",
            "optional_passport_will",
            "previous_state",
            "employer_state",
            "state_id"
    };
    private static final String newNames[] = {
            "ucReceivingPreference",
            "ufPoliticalParty",
            "ufAdditionalInfo",
            "ufSigned",
            "ufAddendum",
            "ufSSN",
            "ufDriverLicense",
            "ufForwardingFax",
            "ufAdditionalInstruction",

            "ufSSN4",
            "ufForHowLong",
            "ufDriversLicense",
            "ufAS_Age",
            "ufEmployerName",
            "ufAS_Attest",
            "ufSSN9",
            "ufAZ_Drivers",
            "ufVA_Available",
            "ufOptionalPassportWillNot",
            "ufOptionalMilitaryIdWillNot",
            "ufOptionalMilitaryIdWill",
            "ufPreviousCounty",
            "ufAZ_Nonop",
            "ufAZ_Indian",
            "ufAZ_Treaty",
            "ufAZ_Tribal",
            "ufAZ_Naturalization",
            "ufVA_Date",
            "ufAdditionalNotes",
            "ufFaxFrom",
            "ufFaxTill",
            "ufEmployerAddress1",
            "ufEmployerAddress2",
            "ufEmployerCity",
            "ufEmployerPostalCode",
            "ufEmployerCountry",
            "ufMilitaryBranch",
            "ufMilitaryRank",
            "ufNoReqSsnWill",
            "ufNoReqPassportWill",
            "ufPassportNumber",
            "ufAdditionalIdPassport",
            "ufAdditionalIdStateId",
            "ufRavaIntentNewAddr",
            "ufRavaIntentBallot",
            "ufFaxForward",
            "ufRavaIntentNewReg",
            "ufRavaIntentNewParty",
            "ufOptionalPassportWill",
            "ufPreviousState",
            "ufEmployerState",
            "ufStateId"
    };


    @Override
	public Integer getChecksum() {
		return CHECK_SUM;
	}

	@Override
	public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
		this.jdbcTemplate = jdbcTemplate;

		log.info("- Start migration...");

		//disableForeignKeyChecks();

        for ( int i = 0; i < oldNames.length; i++) {
            this.jdbcTemplate.update("UPDATE question_fields SET in_pdf_name = ? WHERE in_pdf_name = ?", newNames[i], oldNames[i]);
            this.jdbcTemplate.update("UPDATE pdf_fillings SET in_pdf_name = ? WHERE in_pdf_name = ?",newNames[i], oldNames[i]);
            this.jdbcTemplate.update("UPDATE pdf_fillings SET text = REPLACE(text, ?, ?)", oldNames[i], newNames[i] );
            this.jdbcTemplate.update("UPDATE face_flow_instructions SET text = REPLACE(text, ?, ?)", oldNames[i], newNames[i] );
        }
        this.jdbcTemplate.update("UPDATE pdf_fillings SET text = REPLACE(text, ?, ?)", "$f", "$" );
        this.jdbcTemplate.update("UPDATE pdf_fillings SET text = REPLACE(text, ?, ?)", "$!f", "$!" );
        this.jdbcTemplate.update("UPDATE face_flow_instructions SET text = REPLACE(text, ?, ?)", "$f", "$" );
        this.jdbcTemplate.update("UPDATE face_flow_instructions SET text = REPLACE(text, ?, ?)", "$!f", "$!" );

        this.jdbcTemplate.execute("DELETE from dependents where id in (select id from pdf_fillings where in_pdf_name REGEXP '^[1-9]_[1-9].*')");
        this.jdbcTemplate.execute("DELETE from question_dependencies where variant_id in (select id from pdf_fillings where in_pdf_name REGEXP '^[1-9]_[1-9].*')");
        this.jdbcTemplate.execute("DELETE from pdf_fillings where in_pdf_name REGEXP '^[1-9]_[1-9].*'");

        //enableForeignKeyChecks();
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
