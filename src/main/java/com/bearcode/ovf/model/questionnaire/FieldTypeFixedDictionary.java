package com.bearcode.ovf.model.questionnaire;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 7, 2007
 * Time: 7:20:29 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("FIXED_DICT")
public class FieldTypeFixedDictionary extends FieldType {
    private static final long serialVersionUID = -447936142548945799L;

    public FieldDictionaryItem createGenericItem() {
        return null;
    }

    public Answer createAnswerOfType() {
        return new PredefinedAnswer();
    }


    public void addFiveCents( Map<String, String> model, QuestionField field, Answer answer ) {
        if ( field.getInPdfName().length() == 0 ) return;
        int i = 1;
        for ( FieldDictionaryItem item : getFixedOptions() ) {
            if ( item.getValue().equals( answer.getValue() ) ) break;
            i++;
        }
        model.put( field.getInPdfName() + "_number", String.valueOf(i));
    }

    /**
     * Returns items sorted alphbetically by value
     *
     * @return List<FieldDictionaryItem>
     */
    public List<FieldDictionaryItem> getFixedOptions() {
        List<FieldDictionaryItem> items = super.getFixedOptions();

        Comparator sorter = new Comparator<FieldDictionaryItem>() {
            public int compare( FieldDictionaryItem a, FieldDictionaryItem b ) {
                return a.getValue().compareTo( b.getValue() );
            }
        };
        Collections.sort( items, sorter );

        return items;
    }


}
