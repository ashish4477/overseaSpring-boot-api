package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.*;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.validators.LocalOfficialValidator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 17, 2007
 * Time: 8:38:32 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EodEdit.htm")
public class EditLocalOfficialController extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService( LocalOfficialService localOfficialService ) {
        this.localOfficialService = localOfficialService;
    }

    @Autowired
    private LocalOfficialValidator validator;

    public EditLocalOfficialController() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EodEditLocalOfficial.jsp" );
        setSuccessContentBlock( "/WEB-INF/pages/blocks/admin/EodEditSuccessPage.jsp" );
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setPageTitle( "Edit Local Election Official Information" );
    }

    @ModelAttribute("leo")
    protected LocalOfficial formBackingObject(
            @RequestParam(value = "regionId", required = false, defaultValue = "0") Long regionId,
            @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId ) {
        LocalOfficial leo;
        if ( regionId == 0 ) {
            // create new region
            State state = getStateService().findState( stateId );
            VotingRegion region = new VotingRegion();
            region.setState( state );
            leo = new LocalOfficial();
            leo.setRegion( region );
        }
        else {
            leo = localOfficialService.findForRegion( regionId );
        }
        if ( leo != null ) {
            Officer officerToAdd = new Officer();
            officerToAdd.setOrderNumber( leo.getOfficers().size() + 1 );
            leo.getOfficers().add( officerToAdd );

            leo.sortAdditionalAddresses();
            AdditionalAddress additionalAddressToAdd = new AdditionalAddress();
            additionalAddressToAdd.setAddress( new Address() );
            additionalAddressToAdd.setType( new AdditionalAddressType() );
            leo.getAdditionalAddresses().add( additionalAddressToAdd );
        }
        return leo;
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
            for ( AdditionalAddress additionalAddress : leo.getAdditionalAddresses() ) {
                if ( type.equals( additionalAddress.getType() ) ) {
                    typeIterator.remove();
                    break;
                }
            }
        }
        return addressTypes;
    }

    @ModelAttribute("localOfficeTypes")
    public LocalOfficeType[] getLocalOfficeTypes() {
        return LocalOfficeType.values();
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showLocalOfficial( HttpServletRequest request, ModelMap model,
                                     @ModelAttribute("leo") LocalOfficial leo,
                                     @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId ) {
        if ( leo == null ) {
            if ( stateId != 0 ) {
                return "redirect:/admin/EodVotingRegions.htm?stateId=" + stateId;
            }
            return "redirect:/admin/EodStates.htm";
        }
        model.addAttribute( "votingRegions", localOfficialService.findForState( leo.getRegion().getState() ) );
        model.addAttribute( "additionalAddressTypes", getAdditionalAddressTypes( leo ) );
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String updateLocalOfficial(
            HttpServletRequest request,
            @ModelAttribute("leo") @Valid LocalOfficial command,
            BindingResult errors,
            ModelMap model,
            @RequestParam(value = "deleteAddress", required = false) Long[] toRemove
            ) {
        model.addAttribute( "votingRegions", localOfficialService.findForState( command.getRegion().getState() ) );
        model.addAttribute( "additionalAddressTypes", getAdditionalAddressTypes( command ) );
        List<Long> toRemoves = toRemove != null ? Arrays.asList( toRemove ) : Collections.<Long>emptyList();
        int totalErrors = errors.getErrorCount();
        if ( toRemoves.size() > 0 ) {
            for ( int i = 0; i < command.getAdditionalAddresses().size(); i++ ) {
                AdditionalAddress additionalAddress = command.getAdditionalAddresses().get( i );
                if ( additionalAddress.getId() != 0 && toRemoves.contains( additionalAddress.getId() )) {
                    int filedErrors = errors.getFieldErrorCount( String.format( "additionalAddresses[%d].*", i ) );
                    totalErrors -= filedErrors;
                    continue;
                }
                if ( StringUtils.isBlank( additionalAddress.getType().getName() )
                        && !additionalAddress.getAddress().isEmptySpace() ) {
                    errors.rejectValue( String.format( "additionalAddresses[%d].type.name", i), "mva.address.empty", new String[]{"Type Name"}, "Define additional address type" );
                    totalErrors++;
                }
            }
        }

        if ( request.getParameterMap().containsKey("save") && totalErrors == 0 ) {
            for ( Iterator<AdditionalAddress> addressIterator = command.getAdditionalAddresses().iterator(); addressIterator.hasNext(); ) {
                AdditionalAddress additionalAddress = addressIterator.next();
                if ( additionalAddress.getId() != 0 && toRemoves.contains( additionalAddress.getId() )) {
                    addressIterator.remove();
                    continue;
                }
                if ( additionalAddress.checkEmpty() ) {
                    addressIterator.remove();
                }
            }
            localOfficialService.updateLocalOfficial( command );
            model.addAttribute( "messageCode", "eod.admin.leo.save_success" );
            model.addAttribute( "leo", formBackingObject( command.getRegion().getId(), 0L ) );
            return buildModelAndView( request, model ); //buildSuccessModelAndView( request, model );
        }
        return buildModelAndView( request, model );
    }

    @InitBinder
    protected void initBinder( DataBinder binder ) {
        if ( binder.getTarget() instanceof LocalOfficial ) {
            binder.setValidator( validator );
        }
    }
}
