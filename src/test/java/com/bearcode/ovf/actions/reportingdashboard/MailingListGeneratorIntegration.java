/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import com.bearcode.ovf.dbunittest.OVFDBUnitDataSet;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.OverseasUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Extended {@link BaseReportingDashboardExam} for {@link MailingListGenerator}.
 * 
 * @author IanBrown
 * 
 * @since Apr 19, 2012
 * @version Nov 9, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "MailingListGeneratorIntegration-context.xml" })
public final class MailingListGeneratorIntegration extends BaseReportingDashboardControllerExam<MailingListGenerator> {

	/**
	 * the service used to get the users.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Autowired
	private OverseasUserService overseasUserService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.MailingListGenerator#exportMailingList(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.ui.ModelMap)}
	 * for an administrator.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem processing the request.
	 * @since Apr 19, 2012
	 * @version Nov 9, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/MailingList.xml" })
	public final void testExportMailingList_administrator() throws Exception {
		final OverseasUser adminUser = overseasUserService.findUserById(1l);
		setUpAuthentication(adminUser);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/MailingList.csv");
		request.setMethod("GET");

		getHandlerAdapter().handle(request, response, getBaseController());

		final List<String> actualHeaders = response.getHeaders("Content-Type");
		assertTrue("The content is CSV", actualHeaders.contains("text/csv"));
		final String content = response.getContentAsString();
		assertNotNull("There is content", content);
		final String[] rows = content.split("\n");
		assertEquals("All rows were received", 2, rows.length);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.reportingdashboard.MailingListGenerator#exportMailingList(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.ui.ModelMap)}
	 * for a user with an assigned face.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem processing the request.
	 * @since Apr 19, 2012
	 * @version Nov 9, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/reportingdashboard/MailingList.xml" })
	public final void testExportMailingList_assignedFace() throws Exception {
		final OverseasUser adminUser = overseasUserService.findUserById(2l);
		setUpAuthentication(adminUser);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/reportingdashboard/MailingList.csv");
		request.setMethod("GET");

		getHandlerAdapter().handle(request, response, getBaseController());

		final List<String> actualHeaders = response.getHeaders("Content-Type");
		assertTrue("The content is CSV", actualHeaders.contains("text/csv"));
		final String content = response.getContentAsString();
		assertNotNull("There is content", content);
		final String[] rows = content.split("\n");
		assertEquals("All rows were received", 1, rows.length);
	}

	/** {@inheritDoc} */
	@Override
	protected final MailingListGenerator createBaseReportingDashboardController() {
		final MailingListGenerator mailingListGenerator = applicationContext.getBean(MailingListGenerator.class);
		return mailingListGenerator;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseReportingDashboardController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseReportingDashboardController() {
	}
}
