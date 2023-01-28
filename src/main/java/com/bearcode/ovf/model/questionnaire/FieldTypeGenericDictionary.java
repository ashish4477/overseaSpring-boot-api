package com.bearcode.ovf.model.questionnaire;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 9, 2007
 * Time: 1:21:41 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("GENERIC_DICT")
public class FieldTypeGenericDictionary extends FieldType {
    private static final long serialVersionUID = -7942174090269952808L;

    public FieldDictionaryItem createGenericItem() {
        return new GenericStringItem();
    }

    public Answer createAnswerOfType() {
        return new PredefinedAnswer();
    }

    public void addFiveCents( Map<String, String> model, QuestionField field, Answer answer ) {
        if ( field.getInPdfName().length() == 0 ) return;
        int i = 1;
        for ( FieldDictionaryItem item : field.getGenericOptions() ) {
            if ( item.getValue().equals( answer.getValue() ) ) break;
            i++;
        }
        model.put( field.getInPdfName() + "_number", String.valueOf(i) );
    }

}
