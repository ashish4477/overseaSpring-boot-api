package com.bearcode.ovf.validators;

import com.bearcode.ovf.model.eod.AbstractStateSpecificDirectory;
import com.bearcode.ovf.model.eod.CorrectionsSvid;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Leonid Ginzburg
 */
@Component("svidValidator")
public class StateSpecificDirectoryValidator implements Validator {
    public boolean supports(Class clazz) {
        return clazz.equals(StateSpecificDirectory.class ) || clazz.equals(CorrectionsSvid.class );
    }

    public void validate(Object target, Errors errors) {
        AbstractStateSpecificDirectory svid = (AbstractStateSpecificDirectory) target;
        if ( svid.getWebsite().length() > 0 && !svid.getWebsite().toLowerCase().startsWith("http") ) {
            svid.setWebsite( "http://" + svid.getWebsite() );
        }
        if ( svid instanceof CorrectionsSvid ) {
                StateSpecificDirectory correctionsFor = ((CorrectionsSvid)svid).getCorrectionFor();
                if ( correctionsFor == null ) {
                    errors.reject("","");
                    return;
                }
            if ( ((CorrectionsSvid)svid).checkEmpty() ) {
                errors.reject("eod.corrections.empty");
            }
        }
        if ( svid.getStateContact().getFirstName().length() >= 255 ) {
            errors.rejectValue("stateContact.firstName","mva.address.255_char_max");
        }
        if ( svid.getStateContact().getLastName().length() >= 255 ) {
            errors.rejectValue("stateContact.lastName","mva.address.255_char_max");
        }
        if ( svid.getStateContact().getTitle().length() >= 255 ) {
            errors.rejectValue("stateContact.title","mva.address.255_char_max");
        }

    }


}
