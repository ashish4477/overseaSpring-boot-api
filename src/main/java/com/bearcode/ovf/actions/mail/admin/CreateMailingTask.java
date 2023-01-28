package com.bearcode.ovf.actions.mail.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.editor.MailTemplatePropertyEditor;
import com.bearcode.ovf.editor.MailingListPropertyEditor;
import com.bearcode.ovf.editor.ReportDateEditor;
import com.bearcode.ovf.model.mail.MailTemplate;
import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.model.mail.MailingTask;
import com.bearcode.ovf.service.MailingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author leonid.
 */
@Controller
@RequestMapping("/admin/CreateMailingTask.htm")
public class CreateMailingTask extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    @Qualifier("validator")
    private Validator validator;

    @Autowired
    private MailingListPropertyEditor mailingListPropertyEditor;

    @Autowired
    private MailTemplatePropertyEditor mailTemplatePropertyEditor;

    public CreateMailingTask() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/MailingTaskEdit.jsp" );
        setPageTitle( "Edit Mail Template" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("task")
    public MailingTask createTask( @RequestParam(value = "listId",required = false) Long listId,
                                   @RequestParam(value = "taskId",required = false) Long taskId ) {
        MailingTask task = new MailingTask();
        if ( listId != null && listId != 0 ) {
            MailingList mailingList = mailingListService.findMailingList( listId );
            task.setMailingList( mailingList );
        }
        else if ( taskId != null && taskId > 0 ) {
            MailingTask oldTask = mailingListService.findMailingTaskById( taskId );
            if ( oldTask != null ) return oldTask;
        }
        return task;
    }

    @ModelAttribute("campaigns")
    public List<MailingList> getMailingLists() {
        return mailingListService.findAllMailingLists();
    }

    @ModelAttribute("templates")
    public List<MailTemplate> getTemplates() {
        return mailingListService.findAllTemplates();
    }

    @InitBinder
    public void initBinder( ServletRequestDataBinder binder ) {
        if ( binder.getTarget() instanceof MailingTask ) {
            binder.setValidator( validator );
            binder.registerCustomEditor( MailingList.class, mailingListPropertyEditor );
            binder.registerCustomEditor( MailTemplate.class, mailTemplatePropertyEditor );
            binder.registerCustomEditor( Date.class, new ReportDateEditor() );
        }
    }

    @RequestMapping( method = {RequestMethod.GET, RequestMethod.POST})
    public String showForm( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping( method = RequestMethod.POST, params = "save")
    public String saveTask( HttpServletRequest request, ModelMap model,
                            @ModelAttribute("task") @Valid MailingTask task,
                            BindingResult errors ) {
        if ( !errors.hasErrors() ) {
            task.setStatus( MailingTask.STATUS_READY );
            mailingListService.saveMailingTask( task );
            return String.format( "redirect:/admin/EditMailingList.htm?id=%d", task.getMailingList().getId() );
        }
        return buildModelAndView( request, model );
    }

}
