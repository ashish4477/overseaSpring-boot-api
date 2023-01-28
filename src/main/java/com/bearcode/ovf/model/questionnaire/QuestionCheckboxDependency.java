package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;

import javax.persistence.*;

/**
 * Date: 13.08.12
 * Time: 21:38
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("CHK")
public class QuestionCheckboxDependency extends BasicDependency {
    private static final long serialVersionUID = -1316106826575470200L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "depends_on_id")
    private Question dependsOn;

    @Column(name = "field_value")
    private String fieldValue;

    public QuestionCheckboxDependency() {
    }

    public QuestionCheckboxDependency( Related dependent, Question dependsOn, String fieldValue ) {
        super( dependent );
        this.dependsOn = dependsOn;
        this.fieldValue = fieldValue;
    }

    public Question getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn( Question dependsOn ) {
        this.dependsOn = dependsOn;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue( String fieldValue ) {
        this.fieldValue = fieldValue;
    }

    public String getConditionName() {
        return fieldValue;
    }

    public String getDependsOnName() {
        return getDependsOn().getName();
    }

    public boolean checkDependency( WizardContext wizardContext ) {
        QuestionField field = dependsOn.getKeyField();
        Answer answer = field != null ? wizardContext.getAnswerByFieldId( field.getId() ) : null;

        return (answer != null) && fieldValue.equals( answer.getValue() );
    }

    public boolean checkGroup( Object factor ) {
        return factor instanceof Question && factor == dependsOn;
    }
}
