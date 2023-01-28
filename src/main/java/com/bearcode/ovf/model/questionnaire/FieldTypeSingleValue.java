package com.bearcode.ovf.model.questionnaire;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 9, 2007
 * Time: 5:50:43 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("SINGLE")
public class FieldTypeSingleValue extends FieldType {
    private static final long serialVersionUID = 896141436045915662L;

    public FieldDictionaryItem createGenericItem() {
        return null;
    }

    public Answer createAnswerOfType() {
        return new EnteredAnswer();
    }
}
