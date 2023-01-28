package com.bearcode.ovf.model.questionnaire;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Leonid Ginzburg
 *         Field Type for showing result of previously answered question
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("REPLICA")
public class FieldTypeReplicator extends FieldType {
    private static final long serialVersionUID = -6175167815506035353L;

    public FieldDictionaryItem createGenericItem() {
        return null;
    }

    public Answer createAnswerOfType() {
        return null;
    }

    @Override
    public boolean isDependenciesAllowed() {
        return true;
    }

    @Override
    public void applyDependency( QuestionField dependent, Answer value ) {
        dependent.setFirstText( value.getValue() );
    }
}
