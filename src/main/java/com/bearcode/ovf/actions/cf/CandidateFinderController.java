package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.cf.CandidateFinderForm;
import com.bearcode.ovf.model.mail.MailingAddress;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.validators.CandidateFinderValidator;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping("/CandidateFinder.htm")
//@SessionAttributes("candidateFinder")
public class CandidateFinderController extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private CandidateFinderValidator validator;

    public CandidateFinderController() {
        setPageTitle( "Candidate Finder" );
        setContentBlock( "/WEB-INF/pages/blocks/CandidateFinder.jsp" );
        setSectionCss( "/css/candidate-finder.css" );
        setSectionName( "rava" );
    }

    @InitBinder
    protected void initBinder( ServletRequestDataBinder binder ) {
        final Object target = binder.getTarget();
        if ( target instanceof CandidateFinderForm ) {
            binder.setValidator( validator );
        }
    }

    @ModelAttribute("candidateFinder")
    public CandidateFinderForm formBackingObject() {
        return new CandidateFinderForm();
    }

    @SuppressWarnings("unchecked")
    @RequestMapping(method = RequestMethod.GET)
    public String showFinderForm( HttpServletRequest request, ModelMap model ) throws Exception {
        FieldType countryField = questionFieldService.getCountryFieldType();

        model.addAttribute( "countries", countryField.getFixedOptions() );
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit( HttpServletRequest request, ModelMap model,
                            @ModelAttribute("candidateFinder") @Valid CandidateFinderForm cff,
                            BindingResult errors ) throws Exception {

        /**
         * NOTE: the CandidateFinderForm validator calls the address lookup service which will populate the form with a 
         * valid address, including all previously empty fields like congressional district and zip4
         */

        request.getSession().setAttribute( "cf_address", cff );
        if ( errors.hasErrors() ) {
            return showFinderForm( request, model );
        }
        if ( cff.isAddToList() ) {
            // check if this email is already on the list
            MailingAddress mailingAddress = mailingListService.findByEmail( cff.getEmail() );
            if ( mailingAddress == null ) {
                mailingAddress = new MailingAddress();
                mailingAddress.setEmail( cff.getEmail() );
            }
            mailingAddress.setUrl( request.getServerName() + request.getRequestURI() );
            mailingAddress.setVotingCity( cff.getAddress().getCity() );
            mailingAddress.setVotingPostalCode( cff.getAddress().getZip() );
            mailingAddress.setState( getStateService().findByAbbreviation( cff.getAddress().getState() ) );
            mailingAddress.setCurrentCountry( cff.getCountry() );
            mailingAddress.setVotingStateName( cff.getAddress().getState() );

            mailingAddress.setFirstName( "" );
            mailingAddress.setLastName( "" );
            mailingAddress.setBirthYear( 1900 );
            mailingAddress.setVoterType( "" );
            mailingAddress.setPhone( "" );

            mailingAddress.setEodRegion( "" );
            mailingAddress.setVotingRegionName( "" );

            mailingAddress.setCurrentAddress(  "" );
            mailingAddress.setCurrentCity( "" );
            mailingAddress.setCurrentPostalCode( "" );
            mailingAddress.setCurrentCountryName( "" );

            if ( mailingAddress.getState() != null ) {
                mailingListService.saveMailingAddress( mailingAddress );
            }
        }
        return "redirect:/CandidateFinderList.htm";
    }

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }

}
