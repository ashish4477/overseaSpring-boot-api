package com.bearcode.ovf.model.questionnaire;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 24, 2007
 * Time: 11:15:05 PM
 *
 * @author Leonid Ginzburg
 */
public class VirtualDictionaryItem extends FieldDictionaryItem {
    private static final long serialVersionUID = -8641437030274170120L;

    private String value;

    public String getOptionValue() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public long getId() {
        return 0;
    }

    public void setId( long id ) {
        // nothing to do - this item is never stored
    }

    public QuestionField getForField() {
        return null;
    }

    public void setForField( QuestionField forField ) {

    }

    public void setValue( String newItem ) {
        value = newItem;
    }

    public String getViewValue() {
        if ( value.matches( SPECIAL_PATTERN ) ) {
            String[] parts = value.split( SPECIAL_DELIMITER );
            if ( parts.length > 1 ) {
                return parts[0];
            }
        }
        return value;
    }

    public String getOutputValue() {
        if ( value.matches( SPECIAL_PATTERN ) ) {
            String[] parts = value.split( SPECIAL_DELIMITER );
            if ( parts.length > 1 ) {
                return parts[1];
            }
        }
        return value;
    }

    public void output( Map<String, String> model, String name, boolean doEscapeXml ) {
        String outputValue = doEscapeXml ? StringEscapeUtils.escapeXml( getOutputValue() ) : Answer.unformatValue( getOutputValue() );
        model.put( name, outputValue );
    }

}
