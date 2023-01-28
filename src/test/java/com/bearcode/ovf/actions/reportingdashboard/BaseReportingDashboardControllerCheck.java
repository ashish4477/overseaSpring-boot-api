/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.service.ReportingDashboardService;

/**
 * Abstract extended {@link BaseControllerCheck} test for implementations of {@link BaseReportingDashboardController}.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of base reporting dashboard controller to test.
 * @since Mar 7, 2012
 * @version Mar 28, 2012
 */
public abstract class BaseReportingDashboardControllerCheck<C extends BaseReportingDashboardController> extends
		BaseControllerCheck<C> {

	/**
	 * the service to support the reporting dashboard.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Mar 7, 2012
	 */
	private ReportingDashboardService reportingDashboardService;

	/**
	 * the expected content block.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 7, 2012
	 */
	private String expectedContentBlock;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.BaseReportingDashboard#addCustomReports(HttpServletRequest)} for the case
	 * where there is a user.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	public final void testAddCustomReports_noUser() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final List<Report> actualCustomReports = getBaseController().addCustomReports(request);

		assertTrue("There are no custom reports", actualCustomReports.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.BaseReportingDashboard#addCustomReports(HttpServletRequest)} for the case
	 * where there is a user.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	public final void testAddCustomReports_user() {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String serverName = "www.server";
		request.setServerName(serverName);
		final String contextPath = "/context_path";
		request.setContextPath(contextPath);
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final Report customReport = createMock("CustomReport", Report.class);
		final List<Report> customReports = Arrays.asList(customReport);
		final FaceConfig currentFace = createMock("CurrentFace", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfig(serverName + contextPath)).andReturn(currentFace);
		EasyMock.expect(getReportingDashboardService().findCustomReports(user, currentFace)).andReturn(customReports);
		replayAll();

		final List<Report> actualCustomReports = getBaseController().addCustomReports(request);

		assertSame("The custom reports are returned", customReports, actualCustomReports);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.BaseReportingDashboard#addStandardReports()} for the case
	 * where there is no user.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	public final void testAddStandardReports_noUser() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final Map<String, Report> actualStandardReports = getBaseController().addStandardReports();

		assertTrue("There are no standard reports", actualStandardReports.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.reportingdashboard.BaseReportingDashboard#addStandardReports()} for the case
	 * where there is a user.
	 * 
	 * @author IanBrown
	 * @since Mar 1, 2012
	 * @version Mar 28, 2012
	 */
	@Test
	public final void testAddStandardReports_user() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final Report standardReport = createMock("StandardReport", Report.class);
		final Map<String, Report> standardReports = new HashMap<String, Report>();
		standardReports.put("StandardReport", standardReport);
		EasyMock.expect(getReportingDashboardService().findStandardReports()).andReturn(standardReports);
		replayAll();

		final Map<String, Report> actualStandardReports = getBaseController().addStandardReports();

		assertSame("There standard reports are returned", actualStandardReports, actualStandardReports);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final C createBaseController() {
		final C baseReportingDashboardController = createBaseReportingDashboardController();
		baseReportingDashboardController.setReportingDashboardService(getReportingDashboardService());
		return baseReportingDashboardController;
	}

	/**
	 * Creates a base reporting dashboard controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the base reporting dashbaord controller.
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	protected abstract C createBaseReportingDashboardController();

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return expectedContentBlock;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return BaseReportingDashboardController.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return BaseReportingDashboardController.DEFAULT_SECTION_NAME;
	}

	/**
	 * Gets the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @return the reporting dashboard service.
	 * @since Jan 4, 2012
	 * @version Mar 7, 2012
	 */
	protected final ReportingDashboardService getReportingDashboardService() {
		return reportingDashboardService;
	}

	/**
	 * Sets the expected content block.
	 * 
	 * @author IanBrown
	 * @param expectedContentBlock
	 *            the expected content block.
	 * @since Mar 1, 2012
	 * @version Mar 7, 2012
	 */
	protected final void setExpectedContentBlock(final String expectedContentBlock) {
		this.expectedContentBlock = expectedContentBlock;
	}

	/**
	 * Sets the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @param reportingDashboardService
	 *            the reporting dashboard service to set.
	 * @since Jan 4, 2012
	 * @version Mar 7, 2012
	 */
	protected final void setReportingDashboardService(final ReportingDashboardService reportingDashboardService) {
		this.reportingDashboardService = reportingDashboardService;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setReportingDashboardService(createMock("ReportingDashboardService", ReportingDashboardService.class));
		setUpForBaseReportingDashboardController();
	}

	/**
	 * Sets up to test the specific type of base reporting dashboard controller.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	protected abstract void setUpForBaseReportingDashboardController();

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		tearDownForBaseReportingDashboardController();
		setReportingDashboardService(null);
		setExpectedContentBlock(null);
	}

	/**
	 * Tears down the set up for testing the specific type of base reporting dashboard controller.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	protected abstract void tearDownForBaseReportingDashboardController();

}