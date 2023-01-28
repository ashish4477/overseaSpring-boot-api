package com.bearcode.ovf.model.questionnaire;

import org.apache.commons.lang.StringEscapeUtils;

import javax.persistence.*;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 9, 2007
 * Time: 2:17:01 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("FIXED")
public class FixedStringItem extends FieldDictionaryItem {
    private static final long serialVersionUID = 8497680882214446937L;

    @Column(name = "string_value")
    private String value;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private FieldType fieldType;

    public QuestionField getForField() {
        return null;
    }

    public void setForField( QuestionField forField ) {

    }

    public String getValue() {
        return value;
    }

    public void setValue( String value ) {
        this.value = value;
    }


    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType( FieldType fieldType ) {
        this.fieldType = fieldType;
    }

    public String getOptionValue() {
        return String.valueOf( getId() );
    }

    public String getViewValue() {
        return value;
    }

    public String getOutputValue() {
        return value;
    }

    public void output( Map<String, String> model, String name, boolean doEscapeXml ) {
        String outputValue = doEscapeXml ? StringEscapeUtils.escapeXml( value ) : Answer.unformatValue( value );
        model.put( name, outputValue );
    }
}
