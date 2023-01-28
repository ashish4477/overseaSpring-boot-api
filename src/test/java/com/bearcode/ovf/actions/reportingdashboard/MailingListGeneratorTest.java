/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.mail.MailingAddress;
import com.bearcode.ovf.service.MailingListService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Extended {@link BaseReportingDashboardControllerCheck} for {@link MailingListGenerator}.
 * 
 * @author IanBrown
 * 
 * @since Apr 19, 2012
 * @version Apr 19, 2012
 */
public final class MailingListGeneratorTest extends BaseReportingDashboardControllerCheck<MailingListGenerator> {

	/**
	 * the service used to get the mailing list.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private MailingListService mailingListService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.MailingListGenerator#exportMailingList(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.ui.ModelMap)}
	 * for the case of an administrative user.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the mailing list.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testExportMailingList_administrativeUser() throws IOException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MailingAddress mailingAddress1 = createMock("MailingListEntry1", MailingAddress.class);
		final MailingAddress mailingAddress2 = createMock("MailingListEntry2", MailingAddress.class);
		final List<MailingAddress> mailingAddress = Arrays.asList( mailingAddress1, mailingAddress2 );
		EasyMock.expect(getMailingListService().findAllMailingAddress()).andReturn( mailingAddress ).anyTimes();
		final String csv = "Row1\nRow2";
		EasyMock.expect(getMailingListService().convertToCSV( mailingAddress )).andReturn(csv).anyTimes();
		replayAll();

		getBaseController().exportMailingList(request, response, model);

		final List<String> actualHeaders = response.getHeaders("Content-Type");
		assertTrue("The content is PDF", actualHeaders.contains("text/csv"));
		final String content = response.getContentAsString();
		assertNotNull("There is content", content);
		final String[] rows = content.split("\n");
		assertEquals("All rows were received", 2, rows.length);
		assertArrayEquals("The CSV is returned", csv.split("\n"), rows);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.MailingListGenerator#exportMailingList(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.ui.ModelMap)}
	 * for the case of a user with an assigned face.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the mailing list.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Test
	public final void testExportMailingList_assignedFace() throws IOException {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final ModelMap model = createModelMap(user, request, null, true, false);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(false).anyTimes();
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_REPORTING_DASHBOARD)).andReturn(true).anyTimes();
		final FaceConfig assignedFace = createMock("AssignedFace", FaceConfig.class);
		EasyMock.expect(user.getAssignedFace()).andReturn(assignedFace).anyTimes();
		final MailingAddress mailingAddress = createMock("MailingListEntry", MailingAddress.class);
		final List<MailingAddress> mailingAddresses = Arrays.asList( mailingAddress );
		EasyMock.expect(getMailingListService().findByFace(assignedFace)).andReturn( mailingAddresses ).anyTimes();
		final String csv = "Row1";
		EasyMock.expect(getMailingListService().convertToCSV( mailingAddresses )).andReturn(csv).anyTimes();
		replayAll();

		getBaseController().exportMailingList(request, response, model);

		final List<String> actualHeaders = response.getHeaders("Content-Type");
		assertTrue("The content is PDF", actualHeaders.contains("text/csv"));
		final String content = response.getContentAsString();
		assertNotNull("There is content", content);
		final String[] rows = content.split("\n");
		assertEquals("All rows were received", 1, rows.length);
		assertEquals("The CSV is returned", csv, rows[0]);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final MailingListGenerator createBaseReportingDashboardController() {
		final MailingListGenerator mailingListGenerator = new MailingListGenerator();
		mailingListGenerator.setMailingListService(getMailingListService());
		return mailingListGenerator;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return MailingListGenerator.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseReportingDashboardController() {
		setMailingListService(createMock("MailingListService", MailingListService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseReportingDashboardController() {
		setMailingListService(null);
	}

	/**
	 * Gets the mailing list service.
	 * 
	 * @author IanBrown
	 * @return the mailing list service.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private MailingListService getMailingListService() {
		return mailingListService;
	}

	/**
	 * Sets the mailing list service.
	 * 
	 * @author IanBrown
	 * @param mailingListService
	 *            the mailing list service to set.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	private void setMailingListService(final MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}
}
