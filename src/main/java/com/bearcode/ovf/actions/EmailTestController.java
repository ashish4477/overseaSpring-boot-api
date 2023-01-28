package com.bearcode.ovf.actions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 1, 2007
 * Time: 8:50:57 PM
 * @author Leonid Ginzburg
 */

@Controller
public class EmailTestController extends BaseController {

    @Autowired
	private EmailService mailService;
    //private String contentUrl;

    public EmailTestController() {
        setPageTitle( "Home" );
        setContentBlock( "/WEB-INF/pages/blocks/Home.jsp" );
        setSectionCss( "/css/home.css" );
        setSectionName( "home" );
        setShowMetaKeywords( true );
        //setContentUrl( "/ajax-node/node/239" );
    }

    public EmailService getMailService(){
        return mailService;
    }

    public void setMailService(EmailService mailService){
        this.mailService = mailService;
    }

    @RequestMapping("/sendEmail.htm")
    public String showPage(HttpServletRequest request, ModelMap model) throws Exception {

        java.net.InetAddress localMachine = java.net.InetAddress.getLocalHost();
        //System.out.println("Hostname of local machine: " + localMachine.getHostName());

        final Email email = Email.builder()
        		.to("dhughes@bear-code.com")
        		.model("firstName", "DAEMMON")
        		.model("year", "2020")
        		.template("WEB-INF/faces/basic/mails/rava_thank_you.xml").build();
        mailService.queue(email);
        
        model.addAttribute("secondaryContent", "");
        return buildModelAndView( request, model );
    }

}
