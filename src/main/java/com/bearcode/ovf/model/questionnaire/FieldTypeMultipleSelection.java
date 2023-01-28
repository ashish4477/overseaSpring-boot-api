package com.bearcode.ovf.model.questionnaire;

/**
 * Date: 25.09.12
 * Time: 19:55
 *
 * @author Leonid Ginzburg
 */
public class FieldTypeMultipleSelection extends FieldType {
    private static final long serialVersionUID = -3935317024812881621L;

    public FieldTypeMultipleSelection() {
        setName( "Multiple Selection" );
        setTemplateName( "multiple_checkboxes" );
        setGenericOptionsAllowed( true );
        setVerificationPatternApplicable( false );
    }

    @Override
    public FieldDictionaryItem createGenericItem() {
        return new GenericStringItem();
    }

    @Override
    public Answer createAnswerOfType() {
        return new MultipleAnswer();
    }
}
