package com.bearcode.commons.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/*
 * Remove tomcat generated sessionId in request URL
 * Ignore sessionId in request URL to prevent session fixation attack 
 * 
 */
public class UrlSessionIdFilter implements Filter {

    @SuppressWarnings("deprecation")
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        if (!(req instanceof HttpServletRequest)) {
            chain.doFilter(req, res);
            return;
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // Redirect requests with JSESSIONID in URL to clean version (old links bookmarked/stored by bots)
        // This is ONLY triggered if the request did not also contain a JSESSIONID cookie! Which should be fine for bots...
        if (request.isRequestedSessionIdFromURL()) {
            String url = request.getRequestURL()
                         .append(request.getQueryString() != null ? "?"+request.getQueryString() : "")
                         .toString();
            // TODO: The url is clean, at least in Tomcat, which strips out the JSESSIONID path parameter automatically (Jetty does not?!)
            response.setHeader("Location", url);
            response.sendError(HttpServletResponse.SC_MOVED_PERMANENTLY);
            return;
        }

        // Prevent rendering of JSESSIONID in URLs for all outgoing links
        HttpServletResponseWrapper wrappedResponse =
            new HttpServletResponseWrapper(response) {
                @Override
                public String encodeRedirectUrl(String url) {
                    return url;
                }

                @Override
                public String encodeRedirectURL(String url) {
                    return url;
                }

                @Override
                public String encodeUrl(String url) {
                    return url;
                }

                @Override
                public String encodeURL(String url) {
                    return url;
                }
            };
        chain.doFilter(req, wrappedResponse);

    }

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}
