package com.bearcode.ovf.actions.mail.admin;

import com.bearcode.commons.util.MapUtils;
import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.editor.FieldTypePropertyEditor;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.mail.FaceMailingList;
import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.tools.GetResponseConnector;
import com.bearcode.ovf.validators.VelocitySintaxValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

/**
 * Date: 05.07.12
 * Time: 23:19
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EditMailingList.htm")
public class EditMailingList extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    @Qualifier("validator")
    private Validator validator;

    @Autowired
    private FieldTypePropertyEditor fieldTypeEditor;

    @Autowired
    private VelocitySintaxValidator velocitySintaxValidator;

    @Autowired
    private GetResponseConnector getResponseConnector;

    public EditMailingList() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EditMailingList.jsp" );
        setPageTitle( "Edit Mailing List" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof MailingList ) {
            binder.setValidator( validator );
            binder.registerCustomEditor( FieldType.class, fieldTypeEditor );
        }
    }

    @ModelAttribute("mailingList")
    public MailingList getMailingList( @RequestParam(value = "id", required = false) Long id ) {
        MailingList mailingList = null;
        if ( id != null && id != 0 ) {
            mailingList = mailingListService.findMailingList( id );
        }
        if ( mailingList == null ) {
            mailingList = new MailingList();
        }
        return mailingList;
    }

    @ModelAttribute("fieldTypes")
    public List<FieldType> getSuitableFieldTypes() {
        List<FieldType> types = new LinkedList<FieldType>();
        for ( FieldType type : questionFieldService.findFieldTypes() ) {
            if ( type.isMailingListSignUp() ) {
                types.add( type );
            }
        }
        return types;
    }

    @ModelAttribute("faces")
    public Collection<FaceConfig> faces() {
        return getFacesService().findAllConfigs();
    }

    @ModelAttribute("selectedFaces")
    public List<FaceConfig> selectedFaces( @RequestParam(value = "id", required = false) Long id ) {
        MailingList mailingList = null;
        if ( id != null && id != 0 ) {
            mailingList = mailingListService.findMailingList( id );
        }
        if ( mailingList != null ) {
            List<FaceConfig> faces = new ArrayList<FaceConfig>();
            for (  FaceMailingList faceMailingList : mailingListService.findFacesForMailingList( mailingList ) ) {
                faces.add( faceMailingList.getFace() );
            }
            return faces;
        }
        return Collections.EMPTY_LIST;
    }

    @Deprecated
    public Map<String,String> getCampaignNames( MailingList mailingList ) {
        if ( mailingList.getApiKey().trim().length() > 0 ) {
            return getResponseConnector.getCampaigns( mailingList.getApiKey() );
        }
        return Collections.emptyMap();
    }


    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public String showMailingList( HttpServletRequest request, ModelMap model,
                                   @ModelAttribute("mailingList") MailingList mailingList ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST, params = "save")
    public String saveMailingList( HttpServletRequest request, ModelMap model,
                                   @ModelAttribute("mailingList") @Valid MailingList mailingList,
                                   BindingResult errors ) {
        ValidationUtils.invokeValidator( velocitySintaxValidator, mailingList, errors );
        if ( !errors.hasErrors() ) {
            mailingListService.saveMailingList( mailingList );
            if ( mailingList.getId() != 0 ) {
                long[] facesId = MapUtils.getLongs( request.getParameterMap(), "faceId", new long[]{} );
                mailingListService.saveMailingListFaces( mailingList, facesId );

            }
            return "redirect:/admin/MailingLists.htm";
        }
        return buildModelAndView( request, model );
    }

}
