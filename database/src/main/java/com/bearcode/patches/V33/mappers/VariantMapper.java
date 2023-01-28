package com.bearcode.patches.V33.mappers;

import com.bearcode.patches.V33.model.Variant;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Date: 01.05.12
 * Time: 0:01
 *
 * @author Leonid Ginzburg
 */
public final class VariantMapper implements RowMapper<Variant> {
    @Override
    public Variant mapRow( ResultSet rs, int i ) throws SQLException {
        Variant variant = new Variant();
        variant.setId( rs.getLong( "id" ) );
        variant.setGroupId( rs.getLong( "question_id" ) );
        return variant;
    }
}
