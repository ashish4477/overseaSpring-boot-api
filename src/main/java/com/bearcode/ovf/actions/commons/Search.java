package com.bearcode.ovf.actions.commons;

import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.service.LocalOfficialService;
import com.google.common.collect.Sets;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/search.htm")
public class Search extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @ModelAttribute("paging")
    protected CommonFormObject formBackingObject() throws Exception {
        CommonFormObject object = new CommonFormObject();
        object.setPageSize( 25 );
        return object;
    }

    @RequestMapping(method = { RequestMethod.GET, RequestMethod.POST })
    public String doSearch(final HttpServletRequest request,
                           final ModelMap model,
                           @RequestParam(value = "q", required = false, defaultValue = "") final String rawSearch,
                           @ModelAttribute("paging") final CommonFormObject paging ) {

        setSectionCss("/css/search.css");
        setSectionName("search");
        setContentBlock("/WEB-INF/pages/blocks/SearchResults.jsp");
        setPageTitle( "Search" );

        model.put("eodResults", eodSearch(rawSearch, paging ));
        model.put("siteResults", siteSearch(request, rawSearch));
        model.put("vhdResults", vhdSearch(rawSearch));
        model.put("searchTerms", rawSearch);

        return buildModelAndView( request, model );
    }

    /**
     * Returns a collection of search results from the EOD
     *
     * @param searchString String to search
     * @param paging info fro paging
     * @return Collection of results
     */
    @SuppressWarnings("unchecked")
    protected Collection eodSearch(final String searchString,
                                   final CommonFormObject paging) {

        final String[] elems = searchString.split("[ ,\\.\\-\\+]");

        final List<String> params = new LinkedList<String>();
        final List<String> states = new LinkedList<String>();

        final Collection<State> statesCol = stateService.findAllStates();

        for ( final String elem : elems ) {
            if ( elem.length() > 0 ) {
                boolean found = false;
                for ( final State s : statesCol ) {
                    if ( elem.equalsIgnoreCase( s.getAbbr() )
                            || elem.equalsIgnoreCase( s.getName() ) ) {
                        states.add( s.getAbbr() );
                        found = true;
                        break;
                    }
                }
                if ( !found && elem.length() > 2 ) {
                    params.add( elem );
                }
            }
        }

        Collection ret = null;
        if ( states.size() > 0 || params.size() > 0 ) {
            ret = localOfficialService.findForSearch( params, states, paging.createPagingInfo() );
        }
        return ret;
    }

    @SuppressWarnings("unchecked")
    public Collection siteSearch( final HttpServletRequest request,
                                  final String searchString ) {

        String serverPath = null;
        try {

            serverPath = /*request.getScheme()
                    +*/ "https://"
                    + request.getServerName()
                    + "/opensearch/node/"
                    + URLEncoder.encode( searchString, "utf-8" );

            final URL feedSource = new URL( serverPath );
            final SyndFeedInput input = new SyndFeedInput();
            final SyndFeed feed = input.build( new XmlReader( feedSource ) );
            final Collection entries = new ArrayList();
            for ( final Object entryObj : feed.getEntries() ) {
                if ( entryObj instanceof SyndEntry ) {
                    final SyndEntry entry = (SyndEntry) entryObj;
                    if ( !entry.getDescription().getValue()
                            .equalsIgnoreCase( "null" ) ) {
                        entries.add( entryObj );
                    }
                }
            }
            return entries;

        } catch ( final MalformedURLException e ) {
            logger.error( String.format( "Exception searching site (%s), searching for (%s)", serverPath, searchString ), e );
        } catch ( final FeedException e ) {
            logger.error( String.format( "Exception searching site (%s), searching for (%s)", serverPath, searchString ), e );
        } catch ( final IOException e ) {
            logger.error( String.format( "Exception searching site (%s), searching for (%s)", serverPath, searchString ), e );
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    public Collection vhdSearch(final String searchString) {

        try {
            final String serverPath = "https://vhd.overseasvotefoundation.org/unified/index.php?_m=knowledgebase&_a=restapi&action=search-knowledgebase&search="
                    + URLEncoder.encode(searchString, "utf-8");

            final URL feedSource = new URL(serverPath);
            final SyndFeedInput input = new SyndFeedInput();
            final SyndFeed feed = input.build(new XmlReader(feedSource));
            final Collection entries = new ArrayList();
            for ( final Object entryObj : feed.getEntries() ) {
                if ( entryObj instanceof SyndEntry ) {
                    final SyndEntry entry = (SyndEntry) entryObj;
                    if ( !entry.getDescription().getValue()
                            .equalsIgnoreCase( "null" ) ) {
                        entries.add( entryObj );
                    }
                }
            }
            return entries;

        } catch (final MalformedURLException e) {
            logger.error("Exception searching site", e);
        } catch (final FeedException e) {
            logger.error("Exception searching site", e);
        } catch (final IOException e) {
            logger.error("Exception searching site", e);
        }

        return null;
    }

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }

}
