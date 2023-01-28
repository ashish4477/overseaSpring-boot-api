package com.bearcode.ovf.actions.scytl;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Login implements Controller {

    //private ScytlService scytlService;
    private QuestionnaireService questionnaireService;

    /*
    public ScytlService getScytlService() {
        return scytlService;
    }

    public void setScytlService(ScytlService scytlService) {
        this.scytlService = scytlService;
    }
	*/
	
	public QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	public void setQuestionnaireService(QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	public ModelAndView handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if(principal == null || !(principal instanceof OverseasUser)){
            return new ModelAndView(new RedirectView( "/Login.htm", true));
        }
        OverseasUser user = (OverseasUser)principal;

        // Get url redirection
        //String redirection = scytlService.buildLoginURL(user);
        //response.sendRedirect(redirection);
 		return null;
	}
}
