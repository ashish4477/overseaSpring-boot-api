package com.bearcode.ovf.model.questionnaire;

import com.bearcode.commons.util.MapUtils;
import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 25.09.12
 * Time: 20:11
 *
 * @author Leonid Ginzburg
 */
public class MultipleAnswer extends Answer {
    private static final long serialVersionUID = -5931396164597199179L;

    private List<FieldDictionaryItem> selectedItems;

    public MultipleAnswer() {
        selectedItems = new LinkedList<FieldDictionaryItem>();
    }

    public MultipleAnswer( Answer answer ) {
        super( answer );
        selectedItems = new LinkedList<FieldDictionaryItem>();
        if ( answer instanceof MultipleAnswer ) {
            selectedItems.addAll( ((MultipleAnswer) answer).getSelectedItems() );
        }
    }

    @Override
    public void parseValue( Map<?, ?> parameterMap ) {
        selectedItems.clear();
        String[] values = MapUtils.getStrings( parameterMap, String.valueOf( getField().getId() ), new String[]{} );
        for ( String value : values ) {
            addValue( value );
        }
    }

    @Override
    public void setValue( String value ) {
        // it will be empty for a while
    }

    @Override
    public String getValue() {
        List<String> selections = new LinkedList<String>();
        for ( FieldDictionaryItem item : selectedItems ) {
            selections.add( item.getOptionValue() );
        }
        Gson gson = new Gson();

        return gson.toJson( selections.toArray() );
    }

    @Override
    public Answer createClone() {
        MultipleAnswer a = new MultipleAnswer();
        a.getSelectedItems().addAll( selectedItems );
        return a;
    }

    @Override
    public void output( Map<String, String> model, boolean doEscapeXml ) {
        if ( getField().getInPdfName().length() > 0 && !selectedItems.isEmpty() ) {
            Pattern pattern = Pattern.compile( "([a-zA-Z_]*)(\\d+)" );
            Matcher matcher = pattern.matcher( getField().getInPdfName() );
            if ( matcher.matches() ) {
                String inPdfName = matcher.group( 1 );
                String inPdfNumber = matcher.group( 2 );
                int number = Integer.parseInt( inPdfNumber );

                for ( FieldDictionaryItem item : selectedItems ) {
                    item.output( model, String.format( "%s%d", inPdfName, number++ ) );
                }
            }
        }
    }

    public List<FieldDictionaryItem> getSelectedItems() {
        return selectedItems;
    }

    public void setSelectedItems( List<FieldDictionaryItem> selectedItems ) {
        this.selectedItems = selectedItems;
    }

    private void addValue( String value ) {
        if ( getField() != null ) {
            for ( FieldDictionaryItem item : getField().getOptions() ) {
                if ( value != null && value.equals( item.getOptionValue() ) ) {
                    selectedItems.add( item );
                    break;
                }
            }
        }
    }

    @Override
    public boolean checkEmptyValue() {
        return !getField().isRequired() || !selectedItems.isEmpty();
    }

    @Override
    public void update( Answer newest ) {
        if ( newest instanceof MultipleAnswer ) {
            selectedItems.clear();
            selectedItems.addAll( ((MultipleAnswer) newest).getSelectedItems() );
        }
    }

    public boolean checkNumberOfChoices() {
        if ( getField().getVerificationPattern().matches( "\\d+" ) ) {
            int maxChoices = Integer.parseInt( getField().getVerificationPattern() );
            return selectedItems.size() <= maxChoices;
        }
        return true;
    }
}
