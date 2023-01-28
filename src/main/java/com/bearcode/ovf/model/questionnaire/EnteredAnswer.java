package com.bearcode.ovf.model.questionnaire;

import com.bearcode.commons.util.MapUtils;
import org.apache.commons.lang.StringEscapeUtils;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 28, 2007
 * Time: 2:53:35 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("ENTER")
public class EnteredAnswer extends Answer {
    private static final long serialVersionUID = 6426119509136485944L;

    @Column(name = "entered_value")
    private String enteredValue;

    @Transient
    private String confirmation;

    public String getEnteredValue() {
        return enteredValue;
    }

    public void setEnteredValue( String enteredValue ) {
        this.enteredValue = enteredValue;
    }


    public void setValue( String value ) {
        if ( value != null ) {
            value = value.trim();
        }
        setEnteredValue( value );
    }

    public void setConfirmationValue( String value ) {
        confirmation = value;
    }

    public String getValue() {
        return getEnteredValue();
    }

    public void output( Map<String, String> model, boolean doEscapeXml) {
        if ( getField().getInPdfName().length() > 0 ) {
            String output = doEscapeXml ? StringEscapeUtils.escapeXml( enteredValue ) : unformatValue( enteredValue );
            model.put( getField().getInPdfName(), StringEscapeUtils.escapeXml( output ) );
        }
    }


    public void parseValue( Map parameterMap ) {
        super.parseValue( parameterMap );
        // look for confirmation
        confirmation = MapUtils.getString( parameterMap, String.valueOf( getField().getId() ) + "_cf", "" );
    }


    public boolean checkConfirmation() {
        if ( getField().getType().getTemplateName().equals( FieldType.TEMPLATE_TEXT_CONFIRM ) ) {
            return getValue().equals( confirmation );
        }
        return super.checkConfirmation();
    }

    @Override
    public Answer createClone() {
        EnteredAnswer a = new EnteredAnswer();
        if ( confirmation != null ) {
            a.setConfirmationValue( confirmation );
        }
        return a;
    }

}
