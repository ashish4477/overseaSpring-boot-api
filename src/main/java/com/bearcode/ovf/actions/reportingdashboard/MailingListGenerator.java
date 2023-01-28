/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bearcode.ovf.model.mail.MailingAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.service.MailingListService;

/**
 * Extended {@link BaseReportingDashboardController} to generate mailing lists.
 * 
 * @author IanBrown
 * 
 * @since Apr 19, 2012
 * @version Apr 19, 2012
 */
@Controller
public class MailingListGenerator extends BaseReportingDashboardController {

	/**
	 * the service used to retrieve the mailing list.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@Autowired
	private MailingListService mailingListService;

	/**
	 * the default content block.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = null;

	/**
	 * the reporting dashboard template.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	static final String DEFAULT_TEMPLATE = "templates/ReportingDashboardTemplate";

	/**
	 * the default page title.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	static final String DEFAULT_PAGE_TITLE = "Reporting Dashboard - Mailing List";

	/**
	 * Constructs a mailing list generator with default values.
	 * 
	 * @author IanBrown
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	public MailingListGenerator() {
		super();
		setContentBlock(DEFAULT_CONTENT_BLOCK);
		setPageTitle(DEFAULT_PAGE_TITLE);
	}

	/**
	 * Exports the mailing list as a CSV file.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param response
	 *            the response.
	 * @param model
	 *            the model.
	 * @throws IOException
	 *             if there is a problem exporting the mailing list.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	@RequestMapping(value = "reportingdashboard/MailingList.csv", method = RequestMethod.GET)
	public void exportMailingList(final HttpServletRequest request, final HttpServletResponse response, final ModelMap model)
			throws IOException {
		List<MailingAddress> mailingAddress = null;

		if (getUser().isInRole(UserRole.USER_ROLE_ADMIN)) {
			mailingAddress = getMailingListService().findAllMailingAddress();

		} else if (getUser().isInRole(UserRole.USER_ROLE_REPORTING_DASHBOARD)) {
			final FaceConfig assignedFace = getUser().getAssignedFace();
			mailingAddress = getMailingListService().findByFace(assignedFace);
		}

		final String csv = getMailingListService().convertToCSV( mailingAddress );
		response.addHeader("Content-Type", "text/csv");
		response.getWriter().append(csv);
	}

	/**
	 * Gets the mailing list service.
	 * 
	 * @author IanBrown
	 * @return the mailing list service.
	 * @since Apr 19, 2012
	 * @version Apr 19, 2012
	 */
	public MailingListService getMailingListService() {
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
	public void setMailingListService(final MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}
}
