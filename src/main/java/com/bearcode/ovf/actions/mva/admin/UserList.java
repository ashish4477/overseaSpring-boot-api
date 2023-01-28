package com.bearcode.ovf.actions.mva.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.UserFilterForm;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.tools.export.ExportUserCsv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 23, 2007
 * Time: 6:07:09 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
public class UserList extends BaseController {

    @Autowired
    private OverseasUserService userService;

    public UserList() {
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
        setPageTitle( "Voter Accounts List" );
        setContentBlock( "/WEB-INF/pages/blocks/admin/MvaUserList.jsp" );
    }

    public void setUserService( OverseasUserService userService ) {
        this.userService = userService;
    }

    @ModelAttribute("userFilter")
    protected UserFilterForm formBackingObject() {
        UserFilterForm object = new UserFilterForm();
        object.setPageSize( 25 );                           // TODO get param from config
        return object;
    }

    @ModelAttribute("roles")
    protected Collection<UserRole> getRoles() {
        return userService.findRoles();
    }

    @RequestMapping("/admin/AccountsList.htm")
    public String showPage( HttpServletRequest request, ModelMap model,
                            @ModelAttribute("userFilter") UserFilterForm filter ) {
        long oldRole = filter.getRoleId();
        // used for export links
        filter.setRoleId( 2 ); // voter users
        model.addAttribute( "exportCount", userService.countUsers( filter ) );
        model.addAttribute( "exportPageSize", ExportUserCsv.ROW_LIMIT );
        filter.setRoleId( oldRole );

        model.addAttribute( "userList", userService.findUsers( filter ) );
        return buildModelAndView( request, model );
    }
}
