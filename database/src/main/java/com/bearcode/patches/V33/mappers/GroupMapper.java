package com.bearcode.patches.V33.mappers;

import com.bearcode.patches.V33.model.QuestionGroup;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 30.04.12
 * Time: 23:55
 *
 * @author Leonid Ginzburg
 */
public final class GroupMapper implements RowMapper<QuestionGroup> {
    @Override
    public QuestionGroup mapRow( ResultSet rs, int i ) throws SQLException {
        QuestionGroup question = new QuestionGroup();
        question.setId( rs.getLong( "id" ) );
        question.setPageId( rs.getLong( "page_id" ) );
        question.setName( rs.getString( "name" ) );
        question.setOrder( rs.getInt( "order" ) );
        question.setTitle( rs.getString( "title" ) );
        return question;
    }
}
