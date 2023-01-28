package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.eod.ElectionView;
import com.bearcode.ovf.webservices.grecaptcha.GReCaptchaService;
import com.bearcode.ovf.webservices.localelections.LocalElectionsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Date: 26.02.14
 * Time: 20:06
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping(value = "/state-elections/state-election-dates-deadlines.htm")
@SessionAttributes(ICaptchaUse.USE_COUNT)
public class ElectionsDatesController extends BaseController implements ICaptchaUse {

    @Autowired
    LocalElectionsService localElectionsService;

    @Autowired
    private GReCaptchaService reCaptchaService;

    private boolean electionLabelWiseDataExist;
    private boolean electionDataRequested;

    public ElectionsDatesController() {
        setContentBlock("/WEB-INF/pages/blocks/EodElectionsDates.jsp");
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setCustomPageTitle("Election Dates and Deadlines, When is the Next Election? U.S. Vote Foundation");
        setMetaDescription("When is the next election? Find out with our comprehensive list of election dates and deadlines by state. Up to date voter information.");
    }


    @ModelAttribute("localElections")
    public Collection<? extends ElectionView> getLocalElections(
            @RequestParam(value = "stateName", required = false) String stateName,
            @RequestParam(value = "electionLevel", required = false) String electionLevel) {
        this.electionDataRequested = !StringUtils.isEmpty(electionLevel);
        this.electionLabelWiseDataExist = false;
        if ( stateName != null && stateName.length() > 0 ) {
            List<ElectionView> electionViewList = localElectionsService.findElectionsOfState( stateName );
            if(electionLevel != null){
                List<ElectionView> federalAndState = new ArrayList<ElectionView>();
                List<ElectionView> local = new ArrayList<ElectionView>();
                for (ElectionView electionView : electionViewList){
                    if(electionView.getElectionLevel().getName().equalsIgnoreCase("Federal") ||
                            electionView.getElectionLevel().getName().equalsIgnoreCase("State")){
                        federalAndState.add(electionView);
                    } else{
                        local.add(electionView);
                    }
                }
                if(electionLevel.equalsIgnoreCase("Federal and State")){
                    this.electionLabelWiseDataExist = federalAndState.size() > 0;
                }

                if(electionLevel.equalsIgnoreCase("Local")){
                    this.electionLabelWiseDataExist = local.size() > 0;
                }
            }
            return electionViewList;
        }
        return Collections.emptyList();
    }

   
    
    @ModelAttribute(USE_COUNT)
    public Integer getUseCountAttribute() {
        return number4Captcha;
    }

    @RequestMapping("")
    public String showElections( final HttpServletRequest request, final ModelMap model,
                                 @RequestParam(value = CAPTHCA_PARAM, required = false) String captchaInput,
                                 @RequestParam(value = "electionLevel", required = false) String electionLevel,
                                 final HttpSession theSession,
                                 @ModelAttribute("user") final OverseasUser user,
                                 @ModelAttribute(USE_COUNT) Integer useCount ) {
        FaceConfig faceConfig = getFaceConfig(request);
        if ( faceConfig.isUseCaptcha() && user == null ) {
            if ( useCount == null || useCount <= 0 ) {
                if ( captchaInput != null ) {
                    //captchaInput = captchaInput.toLowerCase();
                    if ( !captchaInput.equals( "" ) && reCaptchaService.verifyCaptcha( captchaInput, faceConfig.getRelativePrefix() ) ) {
                        model.addAttribute( USE_COUNT, number4Captcha );
                        useCount = number4Captcha;
                    }
                }
            }
            else {
                model.addAttribute( USE_COUNT, --useCount );
            }
            if ( useCount == null || useCount <= 0 ) {
                model.addAttribute( "showCaptcha", true );
            }
        }
        if ( electionLevel != null ) {
        	model.addAttribute( "electionLevel", electionLevel );
        }

        model.addAttribute("resultsRequested", this.electionDataRequested);
        model.addAttribute("resultExist", this.electionLabelWiseDataExist);

        return buildModelAndView( request, model );
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE)
    public String getSiteKey() {
        return reCaptchaService.getGoogleSiteKey();
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE_SKIMM)
    public String getSiteKeySkim(){
        return reCaptchaService.getGoogleSiteKeyForSkimm();
    }

    @Override
    @ModelAttribute(SITE_KEY_ATTRIBUTE_VOTE411)
    public String getSiteKeyVote411(){
        return reCaptchaService.getGoogleSiteKeyForVote411();
    }
}
