package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.utils.SecurityContextHelper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by leonid on 22.10.15.
 */
@Controller
@RequestMapping("/w/RestartNew.htm")
public class RestartNewRegistration {


    @RequestMapping("")
    public String restartFlow( HttpServletRequest request ) {

        OverseasUser user = SecurityContextHelper.getUser();

        WizardContext context = SessionContextStorage.create( request ).load();
        if ( context != null ) {
            FlowType flow = context.getFlowType();

            if ( user == null ) {
                // anonymous user - just restart context
                SessionContextStorage.create( request, context ).delete();
            }
            else {
                // logged in user - do logout and restart
                SecurityContextHolder.getContext().setAuthentication( null );
                SessionContextStorage.create( request, context ).delete();
            }
            return String.format( "redirect:/w/%s.htm", flow.name().toLowerCase() );
        }
        return "redirect:/home.htm";
    }
}