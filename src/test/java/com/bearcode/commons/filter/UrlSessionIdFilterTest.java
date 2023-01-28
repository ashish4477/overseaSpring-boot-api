/**
 * 
 */
package com.bearcode.commons.filter;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link UrlSessionIdFilter}.
 * 
 * @author IanBrown
 * 
 * @since Dec 19, 2011
 * @version Dec 19, 2011
 */
public final class UrlSessionIdFilterTest extends EasyMockSupport {

	/**
	 * the URL session ID filter to test.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private UrlSessionIdFilter urlSessionIdFilter;

	/**
	 * Sets up the URL session id filter to test.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Before
	public final void setUpUrlSessionIdFilter() {
		setUrlSessionIdFilter(createUrlSessionIdFilter());
	}

	/**
	 * Tears down the URL session id filter after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@After
	public final void tearDownUrlSessionIdFilter() {
		setUrlSessionIdFilter(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.UrlSessionIdFilter#destroy()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testDestroy() {
		getUrlSessionIdFilter().destroy();

		assertTrue("Nothing was done", true);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.UrlSessionIdFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws ServletException
	 *             if there is a problem with the servlet.
	 * @throws IOException
	 *             if there is a problem with the URL.
	 * @throws URISyntaxException
	 *             if there is a problem with the URI.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testDoFilter() throws IOException, ServletException,
			URISyntaxException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final FilterChain chain = createMock("Chain", FilterChain.class);
		chain.doFilter(EasyMock.eq(request),
				(HttpServletResponseWrapper) EasyMock.anyObject());
		replayAll();

		getUrlSessionIdFilter().doFilter(request, response, chain);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.UrlSessionIdFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
	 * for the case where just the chain is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws ServletException
	 *             if there is a problem with the servlet.
	 * @throws IOException
	 *             if there is a problem with the URL.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testDoFilter_chain() throws IOException, ServletException {
		final FilterChain chain = createMock("Chain", FilterChain.class);
		chain.doFilter(null, null);
		replayAll();

		getUrlSessionIdFilter().doFilter(null, null, chain);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.UrlSessionIdFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
	 * for the case where a non-HTTP request is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws ServletException
	 *             if there is a problem with the servlet.
	 * @throws IOException
	 *             if there is a problem with the URL.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testDoFilter_nonHttpRequest() throws IOException,
			ServletException {
		final ServletRequest request = createMock("Request",
				ServletRequest.class);
		final ServletResponse response = createMock("Response",
				ServletResponse.class);
		final FilterChain chain = createMock("Chain", FilterChain.class);
		chain.doFilter(request, response);
		replayAll();

		getUrlSessionIdFilter().doFilter(request, response, chain);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.UrlSessionIdFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
	 * for the case where no request is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws ServletException
	 *             if there is a problem with the servlet.
	 * @throws IOException
	 *             if there is a problem with the URL.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testDoFilter_noRequest() throws IOException,
			ServletException {
		final FilterChain chain = createMock("Chain", FilterChain.class);
		final ServletResponse response = createMock("Response",
				ServletResponse.class);
		chain.doFilter(null, response);
		replayAll();

		getUrlSessionIdFilter().doFilter(null, response, chain);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.UrlSessionIdFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
	 * for the case where nothing is passed in.
	 * 
	 * @author IanBrown
	 * 
	 * @throws ServletException
	 *             if there is a problem with the servlet.
	 * @throws IOException
	 *             if there is a problem with the URL.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test(expected = NullPointerException.class)
	public final void testDoFilter_nothingProvided() throws IOException,
			ServletException {
		getUrlSessionIdFilter().doFilter(null, null, null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.UrlSessionIdFilter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
	 * for the case where the session ID is requested from the URL.
	 * 
	 * @author IanBrown
	 * 
	 * @throws ServletException
	 *             if there is a problem with the servlet.
	 * @throws IOException
	 *             if there is a problem with the URL.
	 * @throws URISyntaxException
	 *             if there is a problem with the URI.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testDoFilter_requestedSessionIdFromUrl()
			throws IOException, ServletException, URISyntaxException {
		final String requestURI = "/requestUri";
		final String queryString = "Query String";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final FilterChain chain = createMock("Chain", FilterChain.class);
		request.setRequestedSessionIdFromURL(true);
		request.setRequestURI(requestURI);
		request.setQueryString(queryString);
		replayAll();

		getUrlSessionIdFilter().doFilter(request, response, chain);

		final URI uri = new URI("http://localhost:80" + requestURI);
		final String uriUrl = uri.toURL().toString();
		final String expectedURL = uriUrl + "?" + queryString;
		assertEquals("The location is set", expectedURL,
				response.getHeader("Location"));
		assertEquals("The response status code is set",
				HttpServletResponse.SC_MOVED_PERMANENTLY, response.getStatus());
		assertTrue("The response is committed", response.isCommitted());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.UrlSessionIdFilter#init(javax.servlet.FilterConfig)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws ServletException
	 *             if there is a problem initializing the filter.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testInit() throws ServletException {
		final FilterConfig filterConfig = createMock("FilterConfig",
				FilterConfig.class);

		getUrlSessionIdFilter().init(filterConfig);

		assertTrue("Nothing was done", true);
	}

	/**
	 * Creates a URL session id filter.
	 * 
	 * @author IanBrown
	 * @return the URL session id filter.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private UrlSessionIdFilter createUrlSessionIdFilter() {
		return new UrlSessionIdFilter();
	}

	/**
	 * Gets the URL session id filter.
	 * 
	 * @author IanBrown
	 * @return the URL session id filter.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private UrlSessionIdFilter getUrlSessionIdFilter() {
		return urlSessionIdFilter;
	}

	/**
	 * Sets the URL session id filter.
	 * 
	 * @author IanBrown
	 * @param urlSessionIdFilter
	 *            the URL session id filter to set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private void setUrlSessionIdFilter(
			final UrlSessionIdFilter urlSessionIdFilter) {
		this.urlSessionIdFilter = urlSessionIdFilter;
	}

}
