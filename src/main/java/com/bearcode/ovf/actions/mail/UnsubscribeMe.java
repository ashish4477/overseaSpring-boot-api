package com.bearcode.ovf.actions.mail;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.mail.MailingLink;
import com.bearcode.ovf.model.mail.MailingLinkStatus;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.utils.CipherAgentException;
import com.bearcode.ovf.utils.CipherAgentUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author leonid.
 */
@Controller
@RequestMapping("/UnSubscribeMe.htm")
public class UnsubscribeMe extends BaseController {

    @Autowired
    private MailingListService mailingListService;

    public UnsubscribeMe() {
        setContentBlock( "/WEB-INF/pages/blocks/Unsubscribe.jsp" );
        setPageTitle( "Unsubcribe" );
        setSectionCss( "/css/login.css" );
        setSectionName( "unsubscribe" );
    }

    @RequestMapping( method = {RequestMethod.GET, RequestMethod.POST})
    public String doAction( HttpServletRequest request, ModelMap model,
                            @RequestParam(value = "key", required = false) String token ) {
        if ( token != null ) {
            try {
                String[] parts = CipherAgentUtils.decodeToken( token, "unsubscribe" ).split( ":" );
                if ( parts.length >= 2 ) {
                    Long mailingListId = Long.parseLong( parts[0] );
                    String email = parts[1];

                    MailingLink link = mailingListService.findLinkByListIdAndEmail( mailingListId, email );

                    link.setStatus( MailingLinkStatus.UNSUBSCRIBED );
                    link.setLastUpdated( new Date() );

                    mailingListService.updateMailingLink( link );
                    model.put( "success", "successfully" );
                }
            } catch (CipherAgentException e) {
                // nothing
            }
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.HEAD)
    @Override
    public ResponseEntity<String> sendMethodNotAllowed() {
        return super.sendMethodNotAllowed();
    }
}
