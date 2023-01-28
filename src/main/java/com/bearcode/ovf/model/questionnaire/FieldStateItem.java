package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.model.common.State;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 9, 2007
 * Time: 1:44:45 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("STATE")
public class FieldStateItem extends FieldDictionaryItem {
    private static final long serialVersionUID = 519594994885092830L;

    @ManyToOne
    @JoinColumn(name = "external_id")
    private State state;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private FieldType fieldType;


    public QuestionField getForField() {
        return null;
    }

    public void setForField( QuestionField forField ) {

    }

    public void setValue( String newItem ) {

    }

    public State getState() {
        return state;
    }

    public void setState( State state ) {
        this.state = state;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType( FieldType fieldType ) {
        this.fieldType = fieldType;
    }

    public String getOptionValue() {
        return String.valueOf( getState().getId() );
    }

    public String getValue() {
        return state.getName();
    }


    public String getViewValue() {
        return state.getName();
    }

    public String getOutputValue() {
        return state.getName();
    }

    public void output( Map<String, String> model, String name, boolean doEscapeXml ) {
        model.put( name, state.getName() );
        model.put( name + "_abbr", state.getAbbr() );
    }
}
