package com.bearcode.patches.V33.mappers;

import com.bearcode.patches.V33.model.Page;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 30.04.12
 * Time: 23:49
 *
 * @author Leonid Ginzburg
 */
public final class PageMapper implements RowMapper<Page> {
    @Override
    public Page mapRow( ResultSet rs, int i ) throws SQLException {
        Page page = new Page();
        page.setId( rs.getLong( "id" ) );
        page.setNumber( rs.getInt( "number" ) );
        page.setStepNumber( rs.getInt( "stepNumber" ) );
        page.setPopupBubble( rs.getString( "popupBubble" ) );
        page.setTitle( rs.getString( "title" ) );
        page.setFormType( rs.getString( "form_type" ) );
        return page;
    }
}
