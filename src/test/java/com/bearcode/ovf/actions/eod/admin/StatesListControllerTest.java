/**
 * 
 */
package com.bearcode.ovf.actions.eod.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.forms.CommonFormObject;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.LocalOfficialService;

/**
 * Extended {@link BaseControllerCheck} test for {@link StatesListController}.
 * 
 * @author IanBrown
 * 
 * @since Jul 26, 2012
 * @version Jul 26, 2012
 */
public final class StatesListControllerTest extends BaseControllerCheck<StatesListController> {

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * Test method for {@link com.bearcode.ovf.actions.eod.admin.StatesListController#getPaging()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testGetPaging() {
		replayAll();

		final CommonFormObject actualPaging = getBaseController().getPaging();

		assertNotNull("A paging object is returned", actualPaging);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.StatesListController#getStateStatistics(com.bearcode.ovf.forms.CommonFormObject)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	public final void testGetStateStatistics() {
		final CommonFormObject paging = createMock("Paging", CommonFormObject.class);
		final Map<String, Object> stateStatistic = new HashMap<String, Object>();
		stateStatistic.put("statistic", "value");
		final Collection<Map<String, Object>> stateStatistics = Arrays.asList(stateStatistic);
		EasyMock.expect(getLocalOfficialService().findStateStatistics(paging)).andReturn(stateStatistics);
		replayAll();

		final Collection<Map<String, Object>> actualStateStatistics = getBaseController().getStateStatistics(paging);

		assertSame("The state statistics are returned", stateStatistics, actualStateStatistics);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.StatesListController#showStates(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testShowStates() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final OverseasUser user = createMock("User", OverseasUser.class);
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, user);
		replayAll();

		final String actualResponse = getBaseController().showStates(request, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final StatesListController createBaseController() {
		final StatesListController statesListController = new StatesListController();
		statesListController.setLocalOfficialService(getLocalOfficialService());
		return statesListController;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return "/WEB-INF/pages/blocks/admin/EodStateList.jsp";
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return "List of States";
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
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
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
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}
}
