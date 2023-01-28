package com.bearcode.ovf.model.questionnaire;

import com.bearcode.commons.util.MapUtils;
import com.google.common.base.MoreObjects;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 28, 2007
 * Time: 2:47:00 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "answers")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class Answer implements Serializable {
    private static final long serialVersionUID = 8393157961830950951L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pdf_answers_id")
    private WizardResults wizardResults;

    @ManyToOne
    @JoinColumn(name = "field_id")
    private QuestionField field;

    abstract public void setValue( String value );

    abstract public String getValue();

    abstract public Answer createClone();

    abstract public void output( Map<String, String> model, boolean doEscapeXml );

    public Answer() {
    }

    // copy ctor
    public Answer( final Answer answer ) {
        setId( null );
        setField( answer.getField() );
        setValue( answer.getValue() );
        setWizardResults( answer.getWizardResults() );
    }

    public Answer clone() {
        final Answer clone = createClone();
        clone.setField( field );
        clone.setValue( getValue() );
        clone.setWizardResults( wizardResults );
        return clone;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public WizardResults getWizardResults() {
        return wizardResults;
    }

    public void setWizardResults( WizardResults wizardResults ) {
        this.wizardResults = wizardResults;
    }

    public QuestionField getField() {
        return field;
    }

    public void setField( QuestionField field ) {
        this.field = field;
    }

    public boolean checkEmptyValue() {
        if ( !field.isRequired() )
            return true;

        return (getValue() != null && !getValue().isEmpty());
    }

    public boolean checkPattern() {
        if ( field.getType().isVerificationPatternApplicable() && !field.getVerificationPattern().isEmpty() && !getValue().isEmpty() ) {
            return getValue().matches( field.getVerificationPattern() );
        }
        return true;
    }

    public boolean checkConfirmation() {
        return true;
    }

    public void update( Answer newest ) {
        setValue( newest.getValue() );
    }

    public void parseValue( Map<?, ?> parameterMap ) {
        String value = MapUtils.getString( parameterMap, String.valueOf( getField().getId() ), "" );
        setValue( value );
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper( this )
                .add( "id", getId() )
                .add( "field", getField() )
                .toString();
    }

    public void output( Map<String, String> model ) {
        output( model, true );
    }

    /**
   	 * Eliminates the formatting for the input value - undoes XML escaping and replaces HTML
   	 * <p>
   	 * </p>
   	 * with a trailing newline.
   	 *
   	 * @author IanBrown
   	 * @param value
   	 *            the value.
   	 * @return the unformatted value.
   	 * @since May 10, 2012
   	 * @version May 11, 2012
   	 */
   	public final static String unformatValue(final String value) {
   		if (value == null) {
   			return value;
   		}

   		final String newValue = value.replaceAll("</p>", "\n")
                   .replaceAll("</?[^>]+>", "")                // remove any html/xml tags
                   .replaceAll("[ \\t\\x0B\\f\\r]{2,}", " ")   // remove extra spaces
                   .replaceAll("[ \\t\\x0B\\f\\r]*\\n\\s*", "\n").trim();   // but keep \n
   		return newValue;
   	}
}
