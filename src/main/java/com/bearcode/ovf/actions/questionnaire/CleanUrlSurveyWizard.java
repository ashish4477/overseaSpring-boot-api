package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Date: 16.06.14
 * Time: 16:11
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping(value = {"/absentee-ballot-request","/register-to-vote",
        "/overseas/voter-registration-absentee-ballot-request","/military/voter-registration-absentee-ballot-request",
        "/overseas/federal-write-in-absentee-ballot","/military/federal-write-in-absentee-ballot"})
public class CleanUrlSurveyWizard extends SurveyWizard {

    private static final String RAVA_URL_TEMPLATE = "/overseas/voter-registration-absentee-ballot-request/step/%d.htm";
    private static final String FWAB_URL_TEMPLATE = "/overseas/federal-write-in-absentee-ballot/step/%d.htm";
    private static final String REGISTRATION_URL_TEMPLATE = "/register-to-vote/step/%d.htm";
    private static final String ABSENTEE_URL_TEMPLATE = "/absentee-ballot-request/step/%d.htm";

    @Override
    @RequestMapping(value = "/start.htm", method = RequestMethod.GET)
    public String handleFlowStartPage( final HttpServletRequest request,
                                       final String flow,
                                       @RequestParam(value = "voterType", required = false) final String voterTypeStr,
                                       @RequestParam(value = "voterClassificationType", required = false) final String voterClassificationTypeStr,
                                       @RequestParam(value = "vrState", required = false) final String votingRegionState,
                                       @RequestParam(value = "vrName", required = false) final String votingRegionName,
                                       @ModelAttribute("wizardContext") final WizardContext wizardContext ) {
        String uriFlow = defineFlowByUri( request );
        return super.handleFlowStartPage(request, uriFlow, voterTypeStr, voterClassificationTypeStr,votingRegionState, votingRegionName, wizardContext);
    }

    @Override
    @RequestMapping(value = "/step/{page}.htm", method = RequestMethod.GET)
    public String handleFlowPageGetMethod( final HttpServletRequest request,
                                           final String flow,
                                           @PathVariable final int page,
                                           @RequestParam(value = "vrState", required = false) final String votingRegionState,
                                           @RequestParam(value = "vrName", required = false) final String votingRegionName,
                                           @ModelAttribute("wizardContext") final WizardContext wizardContext,
                                           @ModelAttribute("user") final OverseasUser user,
                                           final ModelMap model ) throws Exception {
        String uriFlow = defineFlowByUri( request );
        return super.handleFlowPageGetMethod(request, uriFlow, page, votingRegionState, votingRegionName, wizardContext, user, model );
    }

    @Override
    @RequestMapping(value = "/step/{page}.htm", method = RequestMethod.POST)
    public String handleFlowPagePostMethod( final HttpServletRequest request,
                                            final String flow,
                                            @PathVariable final int page,
                                            @ModelAttribute("wizardContext") final WizardContext wizardContext,
                                            BindingResult errors,
                                            final ModelMap model ) throws Exception {
        String uriFlow = defineFlowByUri( request );
        return super.handleFlowPagePostMethod( request, uriFlow, page, wizardContext, errors, model);
    }

    @Override
    @ModelAttribute("wizardContext")
    public WizardContext getWizardContext( final HttpServletRequest request, final String flow ) {
        String uriFlow = defineFlowByUri( request );
        WizardContext context = super.getWizardContext( request, uriFlow );
        context.setWizardUrlTemplate( defineUrlTemplate( request, uriFlow ));
        return context;
    }

    private String defineFlowByUri( final HttpServletRequest request) {
        String uri = request.getServletPath();
        if ( uri.toLowerCase().matches("/absentee-ballot-request.*")) {
            return FlowType.DOMESTIC_ABSENTEE.toString();
        } else if (uri.toLowerCase().matches("/register-to-vote.*")) {
            return FlowType.DOMESTIC_REGISTRATION.toString();
        } else if (uri.toLowerCase().matches("/(overseas|military)/voter-registration-absentee-ballot-request.*")) {
            return FlowType.RAVA.toString();
        } else if (uri.toLowerCase().matches("/(overseas|military)/federal-write-in-absentee-ballot.*")) {
            return FlowType.FWAB.toString();
        }
        return "";
    }

    private String defineUrlTemplate( final HttpServletRequest request, final String flow ) {
        String template = "";
        if ( !flow.isEmpty() ) {
            FlowType type = FlowType.valueOf( flow );
            switch ( type ) {
                case RAVA:
                    template = RAVA_URL_TEMPLATE;
                    if ( request.getServletPath().toLowerCase().contains("military") ) {
                        template = template.replace("overseas", "military");
                    }
                    break;
                case FWAB:
                    template = FWAB_URL_TEMPLATE;
                    if ( request.getServletPath().toLowerCase().contains("military") ) {
                        template = template.replace("overseas", "military");
                    }
                    break;
                case DOMESTIC_REGISTRATION:
                    template = REGISTRATION_URL_TEMPLATE;
                    break;
                case DOMESTIC_ABSENTEE:
                    template = ABSENTEE_URL_TEMPLATE;
                    break;
            }
        }
        return template;
    }

}
