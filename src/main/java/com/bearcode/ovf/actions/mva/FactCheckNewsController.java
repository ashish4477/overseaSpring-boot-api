package com.bearcode.ovf.actions.mva;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.webservices.factcheck.FactCheckService;
import com.bearcode.ovf.webservices.factcheck.model.ArticlesList;
import com.bearcode.ovf.webservices.factcheck.model.Tag;
import com.bearcode.ovf.webservices.factcheck.model.TagsList;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 27.05.14
 * Time: 18:06
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/InTheNews.htm")
public class FactCheckNewsController extends BaseController {

    @Autowired
    private FactCheckService factCheckService;

    private static final int ARTICLES_PER_PAGE = 5;

    public FactCheckNewsController() {
        setSectionCss( "/css/rava.css" );
        setSectionName("mva factcheck news");
        setPageTitle("In The News");
        setContentBlock("/WEB-INF/pages/blocks/InTheNews.jsp");
    }

    @ModelAttribute("locations")
    public Tag[] getLocations() {
        TagsList locationsList = factCheckService.getAllTags( FactCheckService.LOCATIONS );
        return locationsList != null ? locationsList.getObjects() : null;
    }

    @ModelAttribute("issues")
    public Tag[] getIssues() {
        TagsList issuesList = factCheckService.getAllTags( FactCheckService.ISSUES );
        return issuesList != null ? issuesList.getObjects() : null;
    }

    @ModelAttribute("currentYear")
    public int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get( Calendar.YEAR );
    }

    @RequestMapping( "" )
    public String startingView(HttpServletRequest request, ModelMap model,
                               @ModelAttribute("user") OverseasUser user,
                               @ModelAttribute("locations") Tag[] locations ) {
        if ( user == null ) {
            return "redirect:/Login.htm";
        }
        String stateName = null;
        if ( user.getVotingAddress() != null && StringUtils.isNotEmpty( user.getVotingAddress().getState() )) {
            State state = getStateService().findByAbbreviation( user.getVotingAddress().getState() );
            if ( state != null ) {
                stateName = state.getName();
            }
        }
        if ( stateName == null && user.getVotingRegion() != null ) {
            stateName = user.getVotingRegion().getState().getName();
        }

        //VoterType voterType = user.getVoterType();
        Tag stateLocation = null;
        Tag typeLocation = null;
        FaceConfig face = getFaceConfig(request);
        if ( locations != null ) {
            for (Tag location : locations ) {
                //find state location
                if ( stateName != null && location.getName().equalsIgnoreCase( stateName ) ) {
                    stateLocation = location;
                }
                //find type location
                if ( face.getName().equalsIgnoreCase("usvote") ) {
                    if ( location.getName().equalsIgnoreCase("national") ) {
                        typeLocation = location;
                    }
                }
                else {
                    if ( location.getName().equalsIgnoreCase("international") ) {
                        typeLocation = location;
                    }
                }
                if ( typeLocation != null && stateLocation != null ) break;
            }
        }
        if ( stateLocation != null ) {
            model.addAttribute( "stateLocation", stateLocation );
            List<String> params = new LinkedList<String>();
            params.add( String.format(FactCheckService.LIMIT_PARAM, ARTICLES_PER_PAGE ));
            try {
                params.add( String.format( FactCheckService.LOCATIONS_PARAM, URLEncoder.encode(stateLocation.getName().replaceAll(" ", "+"), "UTF-8") ));
            } catch (UnsupportedEncodingException e) {
                //
            }
            ArticlesList articlesList = factCheckService.getArticlesSearch(params);
            //ArticlesList articlesList = factCheckService.getArticlesOfTag(FactCheckService.LOCATIONS, stateLocation.getId(), ARTICLES_PER_PAGE);
            if ( articlesList != null ) {
                model.addAttribute( "stateArticles", factCheckService.getOrderedArticles( articlesList.getObjects() ) );
            }
        }
        if ( typeLocation != null ) {
            model.addAttribute( "typeLocation", typeLocation );
            List<String> params = new LinkedList<String>();
            params.add( String.format(FactCheckService.LIMIT_PARAM, ARTICLES_PER_PAGE ));
            try {
                params.add(String.format(FactCheckService.LOCATIONS_PARAM, URLEncoder.encode(typeLocation.getName().replaceAll(" ", "+"), "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                //
            }
            ArticlesList articlesList = factCheckService.getArticlesSearch( params );
            //ArticlesList articlesList = factCheckService.getArticlesOfTag(FactCheckService.LOCATIONS, typeLocation.getId(), ARTICLES_PER_PAGE);
            if ( articlesList != null ) {
                model.addAttribute( "typeArticles", factCheckService.getOrderedArticles(articlesList.getObjects()));
            }
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String searchResult( HttpServletRequest request, ModelMap model,
                                @RequestParam(value = "issue", required = false, defaultValue = "") String issueName,
                                @RequestParam(value = "location", required = false, defaultValue = "") String locationName,
                                @RequestParam(value = "year", required = false, defaultValue = "") String year,
                                                               @ModelAttribute("user") OverseasUser user,
                                                               @ModelAttribute("locations") Tag[] locations) {
        List<String> params = new LinkedList<String>();
        try {
            if ( !issueName.isEmpty() ) {
                params.add( String.format(FactCheckService.ISSUES_PARAM, URLEncoder.encode(issueName.replaceAll(" ", "+"), "UTF-8") ));
            }
            if ( !locationName.isEmpty() ) {
                params.add( String.format( FactCheckService.LOCATIONS_PARAM, URLEncoder.encode(locationName.replaceAll(" ", "+"), "UTF-8") ));
            }
        } catch (UnsupportedEncodingException e) {
            // if UTF-8 is unsupported we have nothing to do
        }
        if ( !year.isEmpty() ) {
            params.add( String.format( FactCheckService.YEAR_PARAM, year, year ) );
        }
        if ( params.isEmpty() ) {
            return startingView(request, model, user, locations);
        }
        ArticlesList articles = factCheckService.getArticlesSearch( params );
        if ( articles != null ) {
            model.addAttribute( "searchArticles", factCheckService.getOrderedArticles(articles.getObjects()));
            model.addAttribute( "meta", articles.getMeta() );
        }
        return buildModelAndView( request, model );
    }

    @RequestMapping( method = RequestMethod.POST, params = "uri")
    public String viewMoreResults( HttpServletRequest request, ModelMap model,
                                   @RequestParam("uri") String uri ) {
        ArticlesList articles = factCheckService.getArticlesMore(uri);
        if ( articles != null ) {
            model.addAttribute( "searchArticles", factCheckService.getOrderedArticles(articles.getObjects()) );
            model.addAttribute( "meta", articles.getMeta() );
        }
        return buildModelAndView( request, model );
    }

    public void setFactCheckService(FactCheckService factCheckService) {
        this.factCheckService = factCheckService;
    }
}
