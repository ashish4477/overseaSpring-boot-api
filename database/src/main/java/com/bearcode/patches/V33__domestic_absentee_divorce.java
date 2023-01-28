package com.bearcode.patches;

import com.bearcode.patches.V33.mappers.GroupMapper;
import com.bearcode.patches.V33.mappers.PageMapper;
import com.bearcode.patches.V33.mappers.VariantMapper;
import com.bearcode.patches.V33.model.Page;
import com.bearcode.patches.V33.model.QuestionGroup;
import com.bearcode.patches.V33.model.Variant;
import com.googlecode.flyway.core.migration.java.JavaMigration;
import com.googlecode.flyway.core.migration.java.JavaMigrationChecksumProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Date: 30.04.12
 * Time: 23:15
 *
 * @author Leonid Ginzburg
 */
public class V33__domestic_absentee_divorce implements JavaMigration, JavaMigrationChecksumProvider {
    // Update the CHECK_SUM if you make any changes into the database schema
    private static final Integer CHECK_SUM = 0x01012012;

    private static final Log log = LogFactory.getLog( V33__domestic_absentee_divorce.class );

    private JdbcTemplate jdbcTemplate;

    @Override
    public Integer getChecksum() {
        return CHECK_SUM;
    }

    @Override
    public void migrate( JdbcTemplate jdbcTemplate ) throws Exception {
        this.jdbcTemplate = jdbcTemplate;

        log.info( "- Start migration..." );

        //disableForeignKeyChecks();

        // You migration code here...
        // 1) find question groups with variants which depends on absentee flow;
        List<QuestionGroup> groups = findGroups();
        log.info( String.format( "- %d questions groups were found", groups.size() ) );

        for ( QuestionGroup question : groups ) {
            log.info( String.format( "- Working for questions %d", question.getId() ) );
            // 2) find / copy page
            Page targetPage = findOrCreatePage( question.getPageId() );

            // 3) check if group contains only one type variants
            if ( containsOneFlow( question ) ) {
                // 4) change question group
                this.jdbcTemplate.update( "UPDATE questions SET page_id = ? WHERE id = ?", targetPage.getId(), question.getId() );
                log.info( "- Updated (mono questions)" );
            } else {
                // 4) copy questions group
                QuestionGroup changed = findOrCreateQuestion( question, targetPage );
                // 5) change variants
                List<Variant> variants = findVariants( question );
                for ( Variant variant : variants ) {
                    this.jdbcTemplate.update( "UPDATE question_variants SET question_id = ? WHERE id = ?", changed.getId(), variant.getId() );
                }
                log.info( "- Updated (variants were moved)" );
            }

        }

        // 6) delete flow dependencies
        jdbcTemplate.update( "DELETE FROM question_dependencies WHERE kind = 'FLOW' " +
                "AND ( field_value = 'DOMESTIC_REGISTRATION' OR field_value = 'DOMESTIC_ABSENTEE')" );
        log.info( "- Domestic and absentee dependencies were deleted." );
        //enableForeignKeyChecks();

        log.info( "- End migration" );
    }

    private List<Variant> findVariants( QuestionGroup question ) {
        return this.jdbcTemplate.query( "SELECT qv.* FROM question_variants qv " +
                "JOIN question_dependencies qd ON qd.variant_id = qv.id " +
                "WHERE qd.kind = 'FLOW' AND qd.field_value = 'DOMESTIC_ABSENTEE' " +
                "AND qv.question_id = ?",
                new Object[]{question.getId()},
                new VariantMapper()
        );
    }

    private QuestionGroup findOrCreateQuestion( QuestionGroup question, Page targetPage ) {
        QuestionGroup targetQuestion = null;
        try {
            targetQuestion = jdbcTemplate.queryForObject( "SELECT * FROM questions WHERE name = ? AND page_id = ?",
                    new Object[]{question.getName(), targetPage.getId()},
                    new GroupMapper() );
            log.info( String.format( "- Question %d was found", targetQuestion.getId() ) );
        } catch ( DataAccessException e ) {
            log.info( "- Question on Absentee flow was not found. Exception: " + e.getMessage() );
        }
        if ( targetQuestion == null ) {
            QuestionGroup createQuestion = new QuestionGroup();
            createQuestion.copyFrom( question );
            int order = jdbcTemplate.queryForInt( "SELECT max(`order`) FROM questions WHERE page_id = ?", targetPage.getId() );
            createQuestion.setPageId( targetPage.getId() );
            createQuestion.setOrder( order + 1 );
            createQuestion.setId( saveNewQuestion( createQuestion ) );
            targetQuestion = createQuestion;
            log.info( String.format( "- New question %d was created", createQuestion.getId() ) );
        }
        return targetQuestion;
    }

    private long saveNewQuestion( final QuestionGroup createQuestion ) {
        final String INSERT_SQL = "INSERT INTO questions ( page_id, `name`, `order`, title ) " +
                "VALUES( ?, ?, ?, ? )";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement( Connection connection ) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement( INSERT_SQL, new String[]{"id"} );
                        ps.setLong( 1, createQuestion.getPageId() );
                        ps.setString( 2, createQuestion.getName() );
                        ps.setInt( 3, createQuestion.getOrder() );
                        ps.setString( 4, createQuestion.getTitle() );
                        return ps;
                    }
                },
                keyHolder );
        return keyHolder.getKey().longValue();
    }

    private boolean containsOneFlow( QuestionGroup question ) {
        int absenteeVariants = this.jdbcTemplate.queryForInt(
                "SELECT count(DISTINCT qd.variant_id) FROM question_dependencies qd " +
                        "JOIN question_variants qv ON qd.variant_id = qv.id " +
                        "WHERE qv.question_id = ? " +
                        "AND qd.kind = 'FLOW' AND field_value = 'DOMESTIC_ABSENTEE'",
                question.getId() );
        int allVariants = this.jdbcTemplate.queryForInt(
                "SELECT count(DISTINCT id) FROM  question_variants qv " +
                        "WHERE qv.question_id = ? ", question.getId() );
        return absenteeVariants == allVariants;
    }

    private Page findOrCreatePage( Long pageId ) {
        PageMapper pageMapper = new PageMapper();
        Page sourcePage = this.jdbcTemplate.queryForObject( "SELECT * FROM questionary_pages WHERE id = ?",
                new Object[]{pageId},
                pageMapper );
        log.info( String.format( "- Source page id %d", sourcePage.getId() ) );
        Page targetPage = null;
        try {
            targetPage = this.jdbcTemplate.queryForObject( "SELECT * FROM questionary_pages WHERE title = ? AND form_type = 'DOMESTIC_ABSENTEE'",
                    new Object[]{sourcePage.getTitle()},
                    pageMapper );
            log.info( String.format( "- Absentee page %d was found", targetPage.getId() ) );
        } catch ( DataAccessException e ) {
            log.info( "- Page for Absentee flow was not found. Exception: " + e.getMessage() );
        }
        if ( targetPage == null ) {
            // create a new page
            int number = this.jdbcTemplate.queryForInt( "SELECT max(`number`) FROM questionary_pages WHERE form_type = 'DOMESTIC_ABSENTEE'" );
            Page createPage = new Page();
            createPage.copyFrom( sourcePage );
            createPage.setNumber( number + 1 );
            createPage.setFormType( "DOMESTIC_ABSENTEE" );
            createPage.setId( saveNewPage( createPage ) );
            targetPage = createPage;
            log.info( String.format( "- New page %d was created", createPage.getId() ) );
        }
        return targetPage;
    }

    private long saveNewPage( final Page createPage ) {
        final String INSERT_SQL = "INSERT INTO questionary_pages (`number`, title, stepNumber, popupBubble, form_type) " +
                "VALUES( ?, ?, ?, ?, 'DOMESTIC_ABSENTEE')";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement( Connection connection ) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement( INSERT_SQL, new String[]{"id"} );
                        ps.setInt( 1, createPage.getNumber() );
                        ps.setString( 2, createPage.getTitle() );
                        ps.setInt( 3, createPage.getStepNumber() );
                        ps.setString( 4, createPage.getPopupBubble() );
                        return ps;
                    }
                },
                keyHolder );
        return keyHolder.getKey().longValue();
    }

    private List<QuestionGroup> findGroups() {
        return this.jdbcTemplate.query( "SELECT DISTINCT q.* FROM questions q " +
                "JOIN question_variants qv ON qv.question_id = q.id " +
                "JOIN question_dependencies qd ON qd.variant_id = qv.id " +
                "WHERE qd.kind = 'FLOW' AND qd.field_value = 'DOMESTIC_ABSENTEE'",
                new GroupMapper() );
    }

    private void enableForeignKeyChecks() throws SQLException {
        this.jdbcTemplate.execute( "SET FOREIGN_KEY_CHECKS=1" );
        this.jdbcTemplate.execute( "SET UNIQUE_CHECKS=1" );
    }

    private void disableForeignKeyChecks() throws SQLException {
        this.jdbcTemplate.execute( "SET FOREIGN_KEY_CHECKS=0" );
        this.jdbcTemplate.execute( "SET UNIQUE_CHECKS=0" );
    }
}
