package com.bearcode.ovf.model.questionnaire;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author Alexey Polyakov
 *         Date: Sep 21, 2007
 *         Time: 4:21:44 PM
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("DATE")
public class FieldTypeDate extends FieldType {
    private static final long serialVersionUID = -5281094851214535255L;

    //abstract public boolean isGenericOptionsAllowed();
    public FieldDictionaryItem createGenericItem() {
        return null;
    }

    public Answer createAnswerOfType() {
        return new EnteredDateAnswer();
    }
}
