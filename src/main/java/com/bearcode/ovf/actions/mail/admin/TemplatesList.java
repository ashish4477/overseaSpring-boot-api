package com.bearcode.ovf.actions.mail.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.commons.OverseasFormController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.mail.MailTemplate;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.tools.MailTemplateAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 30, 2008
 * Time: 8:17:20 PM
 * @author Leonid Ginzburg
 */

/*
* 	<bean id="mailTemplateList" class="com.bearcode.ovf.actions.mail.admin.TemplatesList"
		parent="overseasForm">
		<property name="formView">
			<value>templates/MainTemplate</value>
		</property>
		<property name="contentBlock">
			<value>/WEB-INF/pages/blocks/admin/MailTemplates.jsp</value>
		</property>
		<property name="pageTitle" value="Mail Template List" />
		<property name="templateAccessService">
			<bean class="com.bearcode.ovf.tools.MailTemplateAccessService" />
		</property>

	</bean>
*/
@Controller
public class TemplatesList extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    private MailTemplateAccessService templateAccessService;

    public TemplatesList() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/MailTemplates.jsp" );
        setPageTitle( "Mail Template List" );
        setSectionCss( "/css/admin.css" );
        setSectionName( "admin" );
    }

    public void setTemplateAccessService(MailTemplateAccessService templateAccessService) {
        this.templateAccessService = templateAccessService;
    }

    @ModelAttribute("allMails")
    public List<MailTemplate> getTemplates() {
        return mailingListService.findAllTemplates();
    }

    @RequestMapping("/admin/MailTemplatesList.htm")
    public String buildReferences(HttpServletRequest request, ModelMap model,
                                  @ModelAttribute("user") OverseasUser user) throws Exception {
/*
        OverseasUser oUser = (OverseasUser) user;
        if (oUser.isInRole(UserRole.USER_ROLE_ADMIN)) {
            Map faceMails = new HashMap();
            for( FaceConfig face : getFacesService().findAllConfigs() ) {
                faceMails.put( face, templateAccessService.getFaceTemplates( face ) );
            }
            model.put( "allMails", faceMails );

        } else if (oUser.isInRole(UserRole.USER_ROLE_FACE_ADMIN)) {
            //String serverPath = request.getServerName() + request.getContextPath();
            //FaceConfig face = getFacesService().findConfig(serverPath);
            if ( oUser.getFaces() != null && oUser.getFaces().size() > 0 ) {
                FaceConfig face = oUser.getFaces().iterator().next();
                model.put( "mails",  templateAccessService.getFaceTemplates( face ) );
            }
        }
*/
        return buildModelAndView( request, model );
    }
}
