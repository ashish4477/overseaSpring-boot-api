package com.bearcode.ovf.actions.questionnaire;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Daemmon Hughes
 */
@Controller
public class WizardRedirects {


	/*
	 * Redirects old Rava URL to new one
	 */
	@RequestMapping(value="/Rava.htm")
	public String redirectOldRava( @RequestParam(value = "fwab",required = false) String fwab) {
		String r = "/w/rava.htm";
		if(fwab != null){
			r = "/w/fwab.htm";
		}
		return "redirect:"+r;
	}
}
