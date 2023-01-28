package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.service.LocalOfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 *
 * @author Leonid Ginzburg
 */
@Controller
public class EodSearch extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @ModelAttribute("paging")
    protected CommonFormObject formBackingObject() {
        final CommonFormObject object = new CommonFormObject();
        object.setPageSize(25);
        return object;
    }

    public EodSearch() {
        setContentBlock( "/WEB-INF/pages/blocks/EodSearch.jsp" );
        setSectionName( "eod" );
        setSectionCss( "/css/eod.css" );
        setPageTitle( "Search" );
    }

    @SuppressWarnings("unchecked")
    @RequestMapping("/EodSearch.htm")
    public String doSearch( final HttpServletRequest request,
                            final ModelMap model,
                            @RequestParam(value = "search", required = false, defaultValue = "") final String rawSearch,
                            @ModelAttribute("paging") final CommonFormObject paging ) {
        final String[] elems = rawSearch.split("[ ,\\.\\-\\+]");

        final List<String> params = new LinkedList<String>();
        final List<String> states = new LinkedList<String>();

        final Collection<State> statesCol = stateService.findAllStates();

        for ( final String elem : elems ) {
            if ( elem.length() > 0 ) {
                if ( elem.matches("[a-zA-Z]{2}") ) {
                    for ( final State state : statesCol ) {
                        if ( elem.equalsIgnoreCase( state.getAbbr() ) ) {
                            states.add( elem );
                            break;
                        }
                    }
                    //otherwise ignore 2 letter words
                } else {
                    params.add( elem );
                }
            }
        }

        if ( states.size() > 0 || params.size() > 0 ) {
            model.addAttribute(
                    "found",
                    localOfficialService.findForSearch( params, states, paging.createPagingInfo() ) );
        }
        model.addAttribute( "searchTerms", rawSearch );

        return buildModelAndView( request, model );
    }

    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService(
            final LocalOfficialService localOfficialService) {
        this.localOfficialService = localOfficialService;
    }

}
