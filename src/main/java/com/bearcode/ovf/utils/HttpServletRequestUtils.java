/**
 * 
 */
package com.bearcode.ovf.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * Class provide utility methods for working with HTTP servlet requests.
 * 
 * @author IanBrown
 * 
 * @since Aug 22, 2012
 * @version Aug 22, 2012
 */
public final class HttpServletRequestUtils {

	/**
	 * Gets the full URL from the request.
	 * 
	 * @author Mat Banik
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @return the full URL.
	 * @since Aug 22, 2012
	 * @version Aug 22, 2012
	 */
	public final static String getURL(final HttpServletRequest request) {
		final String scheme = request.getScheme(); // http
		final String serverName = request.getServerName(); // hostname.com
		final int serverPort = request.getServerPort(); // 80
		final String contextPath = request.getContextPath(); // /mywebapp
		final String servletPath = request.getServletPath(); // /servlet/MyServlet
		final String pathInfo = request.getPathInfo(); // /a/b;c=123
		final String queryString = request.getQueryString(); // d=789

		// Reconstruct original requesting URL
		final StringBuffer url = new StringBuffer();
		url.append(scheme).append("://").append(serverName);

		if ((serverPort != 80) && (serverPort != 443)) {
			url.append(":").append(serverPort);
		}

		url.append(contextPath).append(servletPath);

		if (pathInfo != null) {
			url.append(pathInfo);
		}
		if (queryString != null) {
			url.append("?").append(queryString);
		}
		return url.toString();
	}
}
