package com.bearcode.ovf.model.questionnaire;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA. User: Leo Date: Aug 7, 2007 Time: 7:22:43 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@Table(name = "field_dictionaries")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorColumn(name = "type")
public abstract class FieldDictionaryItem implements Serializable {
    private static final long serialVersionUID = -4437278835877145714L;

    public static final String SPECIAL_PATTERN = "(.+)=(.+)";
    public static final String SPECIAL_DELIMITER = "=";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public abstract String getOptionValue();

    public abstract String getValue();

    public abstract QuestionField getForField();

    public abstract void setForField( QuestionField forField );

    public abstract void setValue( String newItem );

    public abstract String getViewValue();

    public abstract String getOutputValue();

    public abstract void output( Map<String, String> model, String name, boolean doEscapeXml );

    public void output( Map<String, String> model, String name ) {
        output( model, name, true );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + " " + getValue() + " (" + getViewValue() + "/" + getOutputValue() + ")";
    }
}
