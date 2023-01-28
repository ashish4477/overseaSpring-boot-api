package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.ElectionView;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.webservices.localelections.model.LocalElection;
import com.bearcode.ovf.webservices.localelections.model.StateVoterInformation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Date: 20.05.14
 * Time: 23:24
 *
 * @author Leonid Ginzburg
 */
@Controller
@SessionAttributes(ICaptchaUse.USE_COUNT)
@RequestMapping(value = "/state-voting-information/{stateName}")
public class SvidCleanUrlController extends BaseEodController {

    public SvidCleanUrlController() {
        setContentBlock( "/WEB-INF/pages/blocks/EodStart.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/EodDisplay.jsp" );
        setShowEod(false);
    }

    @ModelAttribute(USE_COUNT)
    public Integer getUseCountAttribute() {
        return 0;
    }


    @ModelAttribute("pageUrl")
    public String getPageUrlAndOther( final HttpServletRequest request,
                                      final ModelMap model ) {
        model.addAttribute("useCleanUrl", true);
        model.addAttribute("contextPath", request.getContextPath() );
        return super.getPageUrlAndOther(request, model);
    }

    @ModelAttribute("selectedState")
    public State getSelectedState( @PathVariable(value = "stateName") String stateName ) {
        return getStateService().findStateByAbbreviationOrName( stateName );
    }

    @RequestMapping("")
    public String showSvidPage( final HttpServletRequest request,
                               final ModelMap model,
                               @ModelAttribute("user") final OverseasUser user,
                               @ModelAttribute(USE_COUNT) final Integer useCount,
                               @ModelAttribute("selectedState") final State state) {
        if ( state != null ) {
            //final StateSpecificDirectory svid = getLocalOfficialService().findSvidForState( state );
            final StateVoterInformation svid = getLocalElectionsService().findStateVoterInformation( state.getAbbr() );
            model.addAttribute( "svid", svid );
            final List<ElectionView> localElections = getLocalElectionsService().findElectionsOfState( state.getAbbr() );
            model.addAttribute( "localElections", localElections );

        }
        return super.showResult(request, model, user, useCount);
    }


    @RequestMapping(params = {CAPTHCA_PARAM})
    public String checkCaptcha( final HttpServletRequest request,
                                final ModelMap model,
                                @RequestParam(CAPTHCA_PARAM) String captchaInput,
                                final HttpSession theSession,
                                @ModelAttribute("user") final OverseasUser user,
                                @ModelAttribute(USE_COUNT) Integer useCount,
                                @ModelAttribute("selectedState") final State state ) {
        if ( state != null ) {
            //final StateSpecificDirectory svid = getLocalOfficialService().findSvidForState( state );
            final StateVoterInformation svid = getLocalElectionsService().findStateVoterInformation( state.getAbbr() );
            model.addAttribute( "svid", svid );
        }
        return super.checkCaptcha(request, model, captchaInput, theSession, user, useCount);
    }

}
