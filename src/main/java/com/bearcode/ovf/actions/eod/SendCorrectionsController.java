package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.eod.*;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.validators.LocalOfficialValidator;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 6, 2007
 * Time: 10:16:02 PM
 */
@Controller
@RequestMapping("/eodCorrections.htm")
public class SendCorrectionsController extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private LocalOfficialValidator leoValidator;

    public SendCorrectionsController() {
        setPageTitle( "Send Local Official Corrections" );
        setContentBlock( "/WEB-INF/pages/blocks/EodSendCorrections.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/EodSendCorrectionsSuccess.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
    }

    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService( LocalOfficialService localOfficialService ) {
        this.localOfficialService = localOfficialService;
    }

    @ModelAttribute("correction")
    protected CorrectionsLeo formBackingObject( @RequestParam(value = "leoId", required = false, defaultValue = "0") Long leoId,
                                                @RequestParam(value = "regionId", required = false, defaultValue = "0") Long regionId ) {
        LocalOfficial correctionFor = null;
        if ( leoId != 0 ) {
            correctionFor = localOfficialService.findById( leoId );
        } else {
            if ( regionId != 0 ) {
                correctionFor = localOfficialService.findForRegion( regionId );
            }
        }
        return new CorrectionsLeo( correctionFor );
    }

    //@ModelAttribute("additionalAddressTypes")
    public Collection<AdditionalAddressType> getAdditionalAddressTypes( LocalOfficial leo ) {
        PredefinedAdditionalAddressTypes[] predefinedTypes = PredefinedAdditionalAddressTypes.values();

        Collection<AdditionalAddressType> addressTypes = new ArrayList<AdditionalAddressType>();//localOfficialService.findAdditionalAddressTypes();
        for ( PredefinedAdditionalAddressTypes predefinedType : predefinedTypes ) {
            String predefinedName = predefinedType.getName();
            boolean found = false;
            for ( AdditionalAddressType type : addressTypes ) {
                if ( type.getName().equalsIgnoreCase( predefinedName ) ) {
                    found = true;
                    break;
                }
            }
            if ( !found ) {
                AdditionalAddressType predefined = new AdditionalAddressType();
                predefined.setName( predefinedName );
                addressTypes.add( predefined );
            }
        }
        for ( Iterator<AdditionalAddressType> typeIterator = addressTypes.iterator(); typeIterator.hasNext(); ) {
            AdditionalAddressType type = typeIterator.next();
            if ( leo != null && leo.getAdditionalAddresses() != null ) {
                for ( AdditionalAddress additionalAddress : leo.getAdditionalAddresses() ) {
                    if ( type.equals( additionalAddress.getType() ) ) {
                        typeIterator.remove();
                        break;
                    }
                }
            }
        }
        return addressTypes;
    }

    @InitBinder
    protected void initBinder( DataBinder binder ) {
        if ( binder.getTarget() instanceof CorrectionsLeo ) {
            binder.setValidator( leoValidator );
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showForm( HttpServletRequest request, ModelMap model,
                            @ModelAttribute("correction") CorrectionsLeo command ) {
        if ( command.getCorrectionFor() == null ) {
            return "redirect:/eod.htm"; //If there is no object, go to EOD page.
        }
        model.addAttribute( "additionalAddressTypes", getAdditionalAddressTypes( command.getCorrectionFor() ) );
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    protected String onSubmit( HttpServletRequest request, ModelMap model,
                               @ModelAttribute("correction") @Valid CorrectionsLeo command, BindingResult errors ) {
        model.addAttribute( "additionalAddressTypes", getAdditionalAddressTypes( command.getCorrectionFor() ) );
        if ( !errors.hasErrors() ) {
            localOfficialService.makeCorrections( command );
            model.addAttribute( "messageCode", "eod.corrections.save_success" );
            return buildSuccessModelAndView( request, model );
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }
}
