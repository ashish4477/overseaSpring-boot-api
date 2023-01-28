package com.bearcode.ovf.model.questionnaire;

import org.apache.commons.lang.StringEscapeUtils;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 9, 2007
 * Time: 4:57:20 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("GENERIC")
public class GenericStringItem extends FieldDictionaryItem {
    private static final long serialVersionUID = -4192617371669986814L;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private QuestionField forField;

    @Column(name = "string_value")
    private String value;


    public QuestionField getForField() {
        return forField;
    }

    public void setForField( QuestionField forField ) {
        this.forField = forField;
    }

    public String getValue() {
        return value;
    }

    public void setValue( String value ) {
        this.value = value;
    }


    public String getOptionValue() {
        return String.valueOf( getId() );
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

    public boolean equals( Object o ) {
        if ( this == o ) return true;
        if ( o == null || getClass() != o.getClass() ) return false;

        GenericStringItem that = (GenericStringItem) o;

        if ( value != null ? !value.equals( that.value ) : that.value != null ) return false;

        return true;
    }

    public int hashCode() {
        return (value != null ? value.hashCode() : 0);
    }
}
