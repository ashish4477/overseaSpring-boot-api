package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.ElectionView;
import com.bearcode.ovf.webservices.localelections.LocalElectionsService;
import com.bearcode.ovf.webservices.localelections.model.ElectionLevelSortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author leonid.
 */
@Controller
@RequestMapping(value = "/PrimaryElections.htm")
public class PrimariesChart extends BaseController {

    @Autowired
    LocalElectionsService electionsService;

    @Value("${primariesChart.typeRegexp:Primary|State Primary}")
    private String typeRegexp;

    public PrimariesChart() {
        setContentBlock("/WEB-INF/pages/blocks/PrimariesChart.jsp");
        setSectionCss( "/css/eod.css" );
        setSectionName( "eod" );
        setCustomPageTitle("Primary Election Dates by State | US Vote Foundation");
        setMetaDescription("Find out 2022, 2023, & 2024 Election dates by state. We list all the information you need. Up to date voter information and unmatched comprehensive vote data.");
    }

    @ModelAttribute("primaries")
    public Map<String,ElectionView> getPrimaries() {
        Pattern typePattern = Pattern.compile( typeRegexp );
        Map<String,ElectionView> primaries = new HashMap<String, ElectionView>();
        List<ElectionView> elections = electionsService.getAllElections();
        String stateName = null;
        boolean stateFound = false;
        for ( ElectionView election : elections ) {
            if ( !election.getStateAbbr().equals( stateName ) ) {
                stateName = election.getStateAbbr();
                stateFound = false;
            }
            if ( stateFound ) continue;
            if ( election.getSortOrder() == ElectionLevelSortOrder.FEDERAL_STATE ) {
                String electionType = election.getElectionType().getName();
                if ( typePattern.matcher( electionType ).matches() ) {
                    primaries.put( stateName, election );
                    stateFound = true;
                }
            }
        }
        return primaries;
    }

    @RequestMapping("")
    public String showPage( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    public void setTypeRegexp( String typeRegexp ) {
        this.typeRegexp = typeRegexp;
    }
}
