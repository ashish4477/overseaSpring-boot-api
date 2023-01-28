package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.MultipleAnswer;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 3, 2007
 * Time: 4:10:54 PM
 *
 * @author Leonid Ginzburg
 */
public class AnswerValidator implements Validator {

    static final public String EMPTY_MESSAGE_CODE = "rava.field.is_empty";
    static final public String PATTERN_MESSAGE_CODE = "rava.field.not_match_pattern";
    static final public String COMFIRMATION_MESSAGE_CODE = "rava.field.not_match_confirm";
    public static final String TOO_MANY_MESSAGE_CODE = "rava.field.too_many_choices";


    public boolean supports( Class clazz ) {
        return clazz.equals( Answer.class ) ;
    }

    public void validate( Object target, Errors errors ) {
        if ( target instanceof Answer ) {
            validateAnswer( (Answer) target, errors );
        }
    }

    public void validateAnswer( Answer answer, Errors errors ) {
        if ( !answer.checkEmptyValue() ) {
            errors.rejectValue( "answersAsMap[" + answer.getField().getId() + "]",
                    EMPTY_MESSAGE_CODE, "Field cann't be empty" );
        }
        if ( !answer.checkPattern() ) {
            errors.rejectValue( "answersAsMap[" + answer.getField().getId() + "]",
                    PATTERN_MESSAGE_CODE, "Field doesn't match pattern " );
        }
        if ( !answer.checkConfirmation() ) {
            errors.rejectValue( "answersAsMap[" + answer.getField().getId() + "]",
                    COMFIRMATION_MESSAGE_CODE, "Field doesn't match its confirmation " );
        }

        if ( answer instanceof MultipleAnswer && !((MultipleAnswer) answer).checkNumberOfChoices() ) {
            errors.rejectValue( "answersAsMap[" + answer.getField().getId() + "]",
                    TOO_MANY_MESSAGE_CODE,
                    new Object[]{((MultipleAnswer) answer).getSelectedItems().size(), answer.getField().getVerificationPattern() },
                    "You have selected more options than are allowed" );
        }
    }

}
