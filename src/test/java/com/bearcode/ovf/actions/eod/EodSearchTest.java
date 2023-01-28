/**
 * 
 */
package com.bearcode.ovf.actions.eod;

import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.service.LocalOfficialService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import static org.junit.Assert.*;

/**
 * Extended {@link BaseControllerCheck} test for {@link EodSearch}.
 * 
 * @author IanBrown
 * 
 * @since Dec 22, 2011
 * @version Jan 3, 2012
 */
public final class EodSearchTest extends BaseControllerCheck<EodSearch> {

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.EodSearch#doSearch(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for an empty search term and no states.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testDoSearch_emptySearchTermNoStates() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Collection<State> states = new ArrayList<State>();
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final ModelMap model = createModelMap(null, request, null, true, false);
		final String rawSearch = "";
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		addAttributeToModelMap(model, "searchTerms", rawSearch);
		addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final String actualModelAndView = getBaseController().doSearch(request, model, rawSearch, paging);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.EodSearch#doSearch(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for an empty search term with states.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testDoSearch_emptySearchTermWithStates() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final State state = createMock("State", State.class);
		final Collection<State> states = Arrays.asList(state);
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final String rawSearch = "";
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		addAttributeToModelMap(model, "searchTerms", rawSearch);
		replayAll();

		final String actualModelAndView = getBaseController().doSearch(request, model, rawSearch, paging);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.EodSearch#doSearch(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for a non-state abbreviation search term and no states.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testDoSearch_searchTermNoStates() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Collection<State> states = new ArrayList<State>();
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final ModelMap model = createModelMap(null, request, null, true, false);
		final String rawSearch = "GeneralSearchTerm";
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		addAttributeToModelMap(model, "searchTerms", rawSearch);
		addOverseasUserToAuthentication(authentication, null);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(paging.createPagingInfo()).andReturn(pagingInfo);
		final Collection found = Arrays.asList("Found");
		EasyMock.expect(
				getLocalOfficialService().findForSearch(EasyMock.eq(Arrays.asList(rawSearch)),
						EasyMock.eq(new LinkedList<String>()), EasyMock.eq(pagingInfo))).andReturn(found);
		addAttributeToModelMap(model, "found", found);
		replayAll();

		final String actualModelAndView = getBaseController().doSearch(request, model, rawSearch, paging);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.EodSearch#doSearch(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for a non-state abbreviation search term and states.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testDoSearch_searchTermWithStates() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final State state = createMock("State", State.class);
		final Collection<State> states = Arrays.asList(state);
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final ModelMap model = createModelMap(null, request, null, true, false);
		final String rawSearch = "GeneralSearchTerm";
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		addAttributeToModelMap(model, "searchTerms", rawSearch);
		addOverseasUserToAuthentication(authentication, null);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(paging.createPagingInfo()).andReturn(pagingInfo);
		final Collection found = Arrays.asList("Found");
		EasyMock.expect(
				getLocalOfficialService().findForSearch(EasyMock.eq(Arrays.asList(rawSearch)),
						EasyMock.eq(new LinkedList<String>()), EasyMock.eq(pagingInfo))).andReturn(found);
		addAttributeToModelMap(model, "found", found);
		replayAll();

		final String actualModelAndView = getBaseController().doSearch(request, model, rawSearch, paging);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.EodSearch#doSearch(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for a state abbreviation search term and a matching state.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testDoSearch_stateAbbreviationMatchingState() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final State state = createMock("State", State.class);
		final String stateAbbreviation = "ST";
		EasyMock.expect(state.getAbbr()).andReturn(stateAbbreviation);
		final Collection<State> states = Arrays.asList(state);
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		final ModelMap model = createModelMap(null, request, null, true, false);
		final String rawSearch = stateAbbreviation;
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		addAttributeToModelMap(model, "searchTerms", rawSearch);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(paging.createPagingInfo()).andReturn(pagingInfo);
		final Collection found = Arrays.asList("Found");
		EasyMock.expect(
				getLocalOfficialService().findForSearch(EasyMock.eq(new LinkedList<String>()),
						EasyMock.eq(Arrays.asList(stateAbbreviation)), EasyMock.eq(pagingInfo))).andReturn(found);
		addAttributeToModelMap(model, "found", found);
		replayAll();

		final String actualModelAndView = getBaseController().doSearch(request, model, rawSearch, paging);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.EodSearch#doSearch(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for a state abbreviation search term and no matching states.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testDoSearch_stateAbbreviationNoMatchingStates() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final State state = createMock("State", State.class);
		final String stateAbbreviation = "ST";
		EasyMock.expect(state.getAbbr()).andReturn(stateAbbreviation);
		final Collection<State> states = Arrays.asList(state);
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final ModelMap model = createModelMap(null, request, null, true, false);
		final String rawSearch = "SA";
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		addAttributeToModelMap(model, "searchTerms", rawSearch);
		addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final String actualModelAndView = getBaseController().doSearch(request, model, rawSearch, paging);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.EodSearch#doSearch(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for a state abbreviation search term and no states.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testDoSearch_stateAbbreviationNoStates() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Collection<State> states = new ArrayList<State>();
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final ModelMap model = createModelMap(null, request, null, true, false);
		final String rawSearch = "SA";
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		addAttributeToModelMap(model, "searchTerms", rawSearch);
		addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final String actualModelAndView = getBaseController().doSearch(request, model, rawSearch, paging);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.EodSearch#formBackingObject()}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem creating the form backing object.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testFormBackingObject() throws Exception {
		final CommonFormObject actualFormBackingObject = getBaseController().formBackingObject();

		assertNotNull("A form backing object is returned", actualFormBackingObject);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.EodSearch#getLocalOfficialService()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetLocalOfficialService() {
		final LocalOfficialService actualLocalOfficialService = getBaseController().getLocalOfficialService();

		assertSame("The local official service is set", getLocalOfficialService(), actualLocalOfficialService);
	}

	/** {@inheritDoc} */
	@Override
	protected final EodSearch createBaseController() {
		final EodSearch eodSearch = new EodSearch();
		eodSearch.setLocalOfficialService(getLocalOfficialService());
		return eodSearch;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/EodSearch.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "Search";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return "/css/eod.css";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return "eod";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setLocalOfficialService(null);
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

}
