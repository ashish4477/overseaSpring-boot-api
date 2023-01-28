/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.service.LocalOfficialService;

/**
 * Extended {@link BaseControllerCheck} test for {@link Search}.
 * 
 * @author IanBrown
 * 
 * @since Dec 21, 2011
 * @version Jan 3, 2012
 */
public final class SearchTest extends BaseControllerCheck<Search> {

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private LocalOfficialService localOfficialService;


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.Search#eodSearch(java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for the case where there is an empty search string.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testEodSearch_emptySearchString() {
		final String searchString = "";
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		final Collection<State> states = new ArrayList<State>();
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		replayAll();

		final Collection actualEod = getBaseController().eodSearch(searchString, paging);

		assertNull("There are no EOD results", actualEod);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.Search#eodSearch(java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for the case where there are no matching states.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testEodSearch_noMatchingStates() {
		final String stateAbbreviation = "SA";
		final String stateName = "State Name";
		final String searchString = "SearchString";
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(paging.createPagingInfo()).andReturn(pagingInfo);
		final State state = createMock("State", State.class);
		final Collection<State> states = Arrays.asList(state);
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		EasyMock.expect(state.getAbbr()).andReturn(stateAbbreviation);
		EasyMock.expect(state.getName()).andReturn(stateName);
		final List<String> params = Arrays.asList(searchString);
		final List<String> matchedStates = new ArrayList<String>();
		final Collection eod = new ArrayList();
		EasyMock.expect(
				getLocalOfficialService().findForSearch(EasyMock.eq(params), EasyMock.eq(matchedStates), EasyMock.eq(pagingInfo)))
				.andReturn(eod);
		replayAll();

		final Collection actualEod = getBaseController().eodSearch(searchString, paging);

		assertSame("The local officials are returned", eod, actualEod);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.Search#eodSearch(java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for the case where there are no states.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testEodSearch_noStates() {
		final String searchString = "SearchString";
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(paging.createPagingInfo()).andReturn(pagingInfo);
		final Collection<State> states = new ArrayList<State>();
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		final List<String> params = Arrays.asList(searchString);
		final List<String> matchedStates = new ArrayList<String>();
		final Collection eod = new ArrayList();
		EasyMock.expect(
				getLocalOfficialService().findForSearch(EasyMock.eq(params), EasyMock.eq(matchedStates), EasyMock.eq(pagingInfo)))
				.andReturn(eod);
		replayAll();

		final Collection actualEod = getBaseController().eodSearch(searchString, paging);

		assertSame("The local officials are returned", eod, actualEod);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.Search#eodSearch(java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for the case where the state abbreviation is matched.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testEodSearch_stateAbbreviation() {
		final String stateAbbreviation = "SA";
		final String searchString = stateAbbreviation;
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(paging.createPagingInfo()).andReturn(pagingInfo);
		final State state = createMock("State", State.class);
		final Collection<State> states = Arrays.asList(state);
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		EasyMock.expect(state.getAbbr()).andReturn(stateAbbreviation).atLeastOnce();
		final List<String> params = new ArrayList<String>();
		final List<String> matchedStates = Arrays.asList(stateAbbreviation);
		final Object localOfficial = createMock("LocalOfficial", Object.class);
		final Collection eod = Arrays.asList(localOfficial);
		EasyMock.expect(
				getLocalOfficialService().findForSearch(EasyMock.eq(params), EasyMock.eq(matchedStates), EasyMock.eq(pagingInfo)))
				.andReturn(eod);
		replayAll();

		final Collection actualEod = getBaseController().eodSearch(searchString, paging);

		assertSame("The local officials are returned", eod, actualEod);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.Search#eodSearch(java.lang.String, com.bearcode.ovf.forms.CommonFormObject)}
	 * for the case where the state name is matched.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testEodSearch_stateName() {
		final String stateAbbreviation = "SA";
		final String stateName = "StateName";
		final String searchString = stateName;
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		final PagingInfo pagingInfo = createMock("PagingInfo", PagingInfo.class);
		EasyMock.expect(paging.createPagingInfo()).andReturn(pagingInfo);
		final State state = createMock("State", State.class);
		final Collection<State> states = Arrays.asList(state);
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		EasyMock.expect(state.getAbbr()).andReturn(stateAbbreviation).atLeastOnce();
		EasyMock.expect(state.getName()).andReturn(stateName).atLeastOnce();
		final List<String> params = new ArrayList<String>();
		final List<String> matchedStates = Arrays.asList(stateAbbreviation);
		final Object localOfficial = createMock("LocalOfficial", Object.class);
		final Collection eod = Arrays.asList(localOfficial);
		EasyMock.expect(
				getLocalOfficialService().findForSearch(EasyMock.eq(params), EasyMock.eq(matchedStates), EasyMock.eq(pagingInfo)))
				.andReturn(eod);
		replayAll();

		final Collection actualEod = getBaseController().eodSearch(searchString, paging);

		assertSame("The local officials are returned", eod, actualEod);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.Search#formBackingObject()}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem getting the form backing object.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testFormBackingObject() throws Exception {
		replayAll();

		final CommonFormObject actualFormBackingObject = getBaseController().formBackingObject();

		assertNotNull("A form backing object is returned", actualFormBackingObject);
		verifyAll();
	}


	/** {@inheritDoc} */
	@Override
	protected final Search createBaseController() {
		final Search search = new Search();
		ReflectionTestUtils.setField(search, "localOfficialService", getLocalOfficialService());
		return search;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected String getExpectedSuccessContentBlock() {
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
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
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
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}
}
