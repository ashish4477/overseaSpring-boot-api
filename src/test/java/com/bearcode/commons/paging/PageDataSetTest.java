/**
 * 
 */
package com.bearcode.commons.paging;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;
import java.util.Map;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

/**
 * Test for {@link PageDataSet}.
 * 
 * @author IanBrown
 * 
 * @since Dec 19, 2011
 * @version Dec 19, 2011
 */
public final class PageDataSetTest extends EasyMockSupport {

	/**
	 * the page data set to test.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private PageDataSet pageDataSet;

	/**
	 * Sets up to test the page data set.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Before
	public final void setUpPageDataSet() {
		setPageDataSet(createPageDataSet());
	}

	/**
	 * Tears down the page data set after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@After
	public final void tearDownPageDataSet() {
		setPageDataSet(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#generatePageLink(javax.servlet.http.HttpServletRequest, int)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGeneratePageLink() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final int pageNo = 16;
		final String requestURI = "/requestUri";
		request.setRequestURI(requestURI);
		final String stringParameter = "StringParameter";
		final String stringValue = "StringValue";
		request.setParameter(stringParameter, stringValue);
		final String arrayParameter = "ArrayParameter";
		final String value1 = "Value1";
		final String value2 = "Value2";
		final String[] arrayValues = new String[] { value1, value2 };
		request.setParameter(arrayParameter, arrayValues);

		final String actualPageLink = getPageDataSet().generatePageLink(
				request, pageNo);

		final String expectedPageLink = buildPageLink(pageNo, requestURI,
				stringParameter, stringValue, arrayParameter, value1, value2);
		assertEquals("The page link is generated", expectedPageLink,
				actualPageLink);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#generatePages(javax.servlet.http.HttpServletRequest, int, int)}
	 * for the case where the number of rows is less than the page size.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGeneratePages_rowsLessThanPageSize() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final int rows = 2;
		final int pageSize = 10;
		final String requestURI = "/requestUri";
		request.setRequestURI(requestURI);
		final String stringParameter = "StringParameter";
		final String stringValue = "StringValue";
		request.setParameter(stringParameter, stringValue);
		final String arrayParameter = "ArrayParameter";
		final String value1 = "Value1";
		final String value2 = "Value2";
		final String[] arrayValues = new String[] { value1, value2 };
		request.setParameter(arrayParameter, arrayValues);

		final Map<String, String> actualPages = getPageDataSet().generatePages(
				request, rows, pageSize);

		final Map<String, String> expectedPages = buildExpectedPages(1,
				requestURI, stringParameter, stringValue, arrayParameter,
				value1, value2);
		assertEquals("The pages are generated", expectedPages, actualPages);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#generatePages(javax.servlet.http.HttpServletRequest, int, int)}
	 * for the case where the number of rows is more than the page size.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGeneratePages_rowsMoreThanPageSize() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final int rows = 15;
		final int pageSize = 5;
		final String requestURI = "/requestUri";
		request.setRequestURI(requestURI);
		final String stringParameter = "StringParameter";
		final String stringValue = "StringValue";
		request.setParameter(stringParameter, stringValue);
		final String arrayParameter = "ArrayParameter";
		final String value1 = "Value1";
		final String value2 = "Value2";
		final String[] arrayValues = new String[] { value1, value2 };
		request.setParameter(arrayParameter, arrayValues);

		final Map<String, String> actualPages = getPageDataSet().generatePages(
				request, rows, pageSize);

		final Map<String, String> expectedPages = buildExpectedPages(3,
				requestURI, stringParameter, stringValue, arrayParameter,
				value1, value2);
		assertEquals("The pages are generated", expectedPages, actualPages);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#getCurrentPage(javax.servlet.http.HttpServletRequest)}
	 * .
	 * 
	 * TODO should the current page always be 0? The code as written retrieves
	 * the parameter value, but doesn't return it.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCurrentPage() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final int currentPage = 23;
		request.setParameter(PageDataSet.PAGE_PARAM_NAME,
				Integer.toString(currentPage));

		final int actualPage = getPageDataSet().getCurrentPage(request);

		assertEquals("The page is always zero", 0, actualPage);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#getCurrentPageSize(javax.servlet.http.HttpServletRequest, int)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCurrentPageSize() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final int defaultSize = 108;
		final int pageSize = 972;
		request.setParameter(PageDataSet.PAGE_SIZE_PARAM_NAME,
				Integer.toString(pageSize));

		final int actualPageSize = getPageDataSet().getCurrentPageSize(request,
				defaultSize);

		assertEquals("The page size is returned", pageSize, actualPageSize);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#getCurrentPageSize(javax.servlet.http.HttpServletRequest, int)}
	 * for the case where the page size is not set.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCurrentPageSize_default() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final int defaultSize = 87;

		final int actualPageSize = getPageDataSet().getCurrentPageSize(request,
				defaultSize);

		assertEquals("The default page size is returned", defaultSize,
				actualPageSize);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#getPageParamName()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetPageParamName() {
		final String actualPageParamName = getPageDataSet().getPageParamName();

		assertEquals("The default page param name is set",
				PageDataSet.PAGE_PARAM_NAME, actualPageParamName);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#getPageSizeParamName()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetPageSizeParamName() {
		final String actualPageSizeParamName = getPageDataSet()
				.getPageSizeParamName();

		assertEquals("The default page size param name is set",
				PageDataSet.PAGE_SIZE_PARAM_NAME, actualPageSizeParamName);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#PageDataSet(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testPageDataSetStringString() {
		final String pageParamName = "Page Param Name";
		final String pageSizeParamName = "Page Size Param Name";

		final PageDataSet pageDataSet = new PageDataSet(pageParamName,
				pageSizeParamName);

		assertEquals("The page param name is set", pageParamName,
				pageDataSet.getPageParamName());
		assertEquals("The page size param name is set", pageSizeParamName,
				pageDataSet.getPageSizeParamName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#setPageParamName(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetPageParamName() {
		final String pageParamName = "Page Param Name";

		getPageDataSet().setPageParamName(pageParamName);

		assertEquals("The page param name is set", pageParamName,
				getPageDataSet().getPageParamName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.paging.PageDataSet#setPageSizeParamName(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetPageSizeParamName() {
		final String pageSizeParamName = "Page Size Param Name";

		getPageDataSet().setPageSizeParamName(pageSizeParamName);

		assertEquals("The page size param name is set", pageSizeParamName,
				getPageDataSet().getPageSizeParamName());
	}

	/**
	 * Builds the expected pages.
	 * 
	 * @author IanBrown
	 * @param numberOfPages
	 *            the number of pages.
	 * @param requestURI
	 *            the request URI.
	 * @param stringParameter
	 *            the name of the string parameter.
	 * @param stringValue
	 *            the value of the string parameter.
	 * @param arrayParameter
	 *            the name of the array parameter.
	 * @param value1
	 *            the first value of the array.
	 * @param value2
	 *            the second value of the array.
	 * @return the expected pages.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private Map<String, String> buildExpectedPages(final int numberOfPages,
			final String requestURI, final String stringParameter,
			final String stringValue, final String arrayParameter,
			final String value1, final String value2) {
		final Map<String, String> expectedPages = new LinkedHashMap<String, String>();
		expectedPages.put("1", "");
		for (int pageNo = 1; pageNo < numberOfPages; ++pageNo) {
			expectedPages.put(
					Integer.toString(pageNo + 1),
					buildPageLink(pageNo, requestURI, stringParameter,
							stringValue, arrayParameter, value1, value2));
		}
		return expectedPages;
	}

	/**
	 * Builds a page link from the input values.
	 * 
	 * @author IanBrown
	 * @param pageNo
	 *            the page number.
	 * @param requestURI
	 *            the request URI.
	 * @param stringParameter
	 *            the string parameter name.
	 * @param stringValue
	 *            the string value.
	 * @param arrayParameter
	 *            the array parameter name.
	 * @param value1
	 *            the first array value.
	 * @param value2
	 *            the second array value.
	 * @return the page link.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private String buildPageLink(final int pageNo, final String requestURI,
			final String stringParameter, final String stringValue,
			final String arrayParameter, final String value1,
			final String value2) {
		final String expectedPageLink = requestURI + "?"
				+ PageDataSet.PAGE_PARAM_NAME + "=" + pageNo + "&"
				+ stringParameter + "=" + stringValue + "&" + arrayParameter
				+ "=" + value1 + "&" + arrayParameter + "=" + value2;
		return expectedPageLink;
	}

	/**
	 * Creates a page data set.
	 * 
	 * @author IanBrown
	 * @return the page data set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private PageDataSet createPageDataSet() {
		return new PageDataSet();
	}

	/**
	 * Gets the page data set.
	 * 
	 * @author IanBrown
	 * @return the page data set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private PageDataSet getPageDataSet() {
		return pageDataSet;
	}

	/**
	 * Sets the page data set.
	 * 
	 * @author IanBrown
	 * @param pageDataSet
	 *            the page data set to set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private void setPageDataSet(final PageDataSet pageDataSet) {
		this.pageDataSet = pageDataSet;
	}
}
