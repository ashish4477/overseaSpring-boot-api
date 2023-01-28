package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 13, 2007
 * Time: 4:08:02 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("REG")
public class QuestionDependency extends BasicDependency {
    private static final long serialVersionUID = 1066049168432866687L;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "depends_on_id")
    private Question dependsOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "condition_id")
    private FieldDictionaryItem condition;

    public QuestionDependency() {
    }

    public QuestionDependency( Related dependent, Question dependsOn, FieldDictionaryItem condition ) {
        super( dependent );
        this.dependsOn = dependsOn;
        this.condition = condition;
    }

    public Question getDependsOn() {
        return dependsOn;
    }

    public String getConditionName() {
        return condition.getValue();
    }

    public String getDependsOnName() {
        return getDependsOn().getName();
    }

    public boolean checkDependency( WizardContext wizardContext ) {
        QuestionField field = dependsOn.getKeyField();
        Answer answer = field != null ? wizardContext.getAnswerByFieldId( field.getId() ) : null;

        try {
            return (answer != null) && getCondition().getValue().equals( answer.getValue() );
        } catch ( Exception e ) {
            return false; // Hibernate throws exception if an item for condition is deleted
        }
    }

    public void setDependsOn( Question dependsOn ) {
        this.dependsOn = dependsOn;
    }

    public FieldDictionaryItem getCondition() {
        return condition;
    }

    public void setCondition( FieldDictionaryItem condition ) {
        this.condition = condition;
    }


    public boolean checkGroup( Object factor ) {
        if ( factor instanceof Question ) {
            return factor == dependsOn;
        }
        return false;
    }
}
