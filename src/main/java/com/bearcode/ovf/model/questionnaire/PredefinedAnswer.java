package com.bearcode.ovf.model.questionnaire;

import org.hibernate.LazyInitializationException;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Map;

//import com.sun.jdi.InvocationException;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 28, 2007
 * Time: 2:51:55 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("FIXED")
public class PredefinedAnswer extends Answer {
    private static final long serialVersionUID = 606632224315236345L;

    @ManyToOne
    @JoinColumn(name = "selected_value")
    private FieldDictionaryItem selectedValue = null;

    public PredefinedAnswer() {
        super();
    }

    public PredefinedAnswer( Answer answer ) {
        super( answer );
        if ( answer instanceof PredefinedAnswer) {
            selectedValue = ((PredefinedAnswer) answer).getSelectedValue();
        }
    }

    @Override
    public Answer clone() {
        PredefinedAnswer answer = (PredefinedAnswer) super.clone();
        answer.setSelectedValue( selectedValue );
        return answer;
    }

    public FieldDictionaryItem getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue( FieldDictionaryItem selectedValue ) {
        this.selectedValue = selectedValue;
    }

    public void setValue( String value ) {
        if ( getField() != null && getField().getOptions() != null ) {
            for ( FieldDictionaryItem item : getField().getOptions() ) {
                if ( value != null && value.equals( item.getOptionValue() ) ) {
                    selectedValue = item;
                    return;
                }
            }
        }
        selectedValue = null;
    }

    public String getValue() {
        try {
            return selectedValue.getValue();
        } catch ( LazyInitializationException e ) {
            /* Ignore, due to missing FieldDictionaryItem */
        } catch ( NullPointerException e ) {
            /* Ignore */
        }
        return "";
    }

    public void output( Map<String, String> model, boolean doEscapeXml ) {
        try {
            if ( getField().getInPdfName().length() > 0 && selectedValue != null )
                selectedValue.output( model, getField().getInPdfName(), doEscapeXml );
        } catch ( Exception e ) {
            /* This is a strange exception while making output for mailing list export.
            * Looks like there is 'lost' FieldDictionaryItem
            * */
        }
    }

    @Override
    public Answer createClone() {
        PredefinedAnswer a = new PredefinedAnswer();
        a.setSelectedValue( selectedValue );
        return a;
    }


    public void update( Answer newest ) {
        if ( newest instanceof PredefinedAnswer ) {
            setSelectedValue( ((PredefinedAnswer) newest).selectedValue );
        }
    }
}
