/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.DAO.OverseasUserDAO;
import com.bearcode.ovf.DAO.ReportingDashboardDAO;
import com.bearcode.ovf.actions.commons.BaseControllerExam;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportField;
import com.bearcode.ovf.service.ReportingDashboardService;

/**
 * Abstract extended {@link BaseControllerExam} integration test for implementations of {@link BaseReportingDashboardController}.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of base reporting dashboard controller to test.
 * @since Mar 7, 2012
 * @version Mar 28, 2012
 */
public abstract class BaseReportingDashboardControllerExam<C extends BaseReportingDashboardController> extends
		BaseControllerExam<C> {

	/**
	 * the DAO used to access overseas users.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Mar 7, 2012
	 */
	@Autowired
	private OverseasUserDAO overseasUserDAO;

	/**
	 * the service used to access reporting dashboard objects.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	@Autowired
	private ReportingDashboardService reportingDashboardService;

	/**
	 * Custom assertion to ensure that the correct custom reports are in the model.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user.
	 * @param currentFace
	 *            the current face.
	 * @param model
	 *            the model
	 * @param expectCustomReport
	 *            <code>true</code> if the custom report should be in the model, <code>false</code> if it should not.
	 * @since Jan 4, 2012
	 * @version Mar 28, 2012
	 */
	protected final void assertCustomReports(final OverseasUser user, final FaceConfig currentFace, final ModelMap model,
			final boolean expectCustomReport) {
		final Object reportsObject = model.get("customReports");
		assertTrue(reportsObject + " is a list", reportsObject instanceof List);

		@SuppressWarnings("unchecked")
		final List<Report> actualCustomReports = (List<Report>) reportsObject;
		if (expectCustomReport) {
			final List<Report> expectedCustomReports = getReportingDashboardService().findCustomReports(user, currentFace);
			assertEquals("There are the correct custom reports", expectedCustomReports, actualCustomReports);
		} else {
			assertTrue("There are no custom reports", actualCustomReports.isEmpty());
		}
	}

	/**
	 * Custom assertion to ensure that the standard reports are in the model.
	 * 
	 * @author IanBrown
	 * @param model
	 *            the model
	 * @param expectStandardReport
	 *            <code>true</code> if the standard report should be in the model, <code>false</code> if it should not.
	 * @since Jan 4, 2012
	 * @version Mar 9, 2012
	 */
	protected final void assertStandardReports(final ModelMap model, final boolean expectStandardReport) {
		final Object reportsObject = model.get("standardReports");
		assertTrue(reportsObject + " is a map", reportsObject instanceof Map);

		@SuppressWarnings("unchecked")
		final Map<String, Report> actualStandardReports = (Map<String, Report>) reportsObject;
		if (expectStandardReport) {
			final Map<String, Report> expectedStandardReports = getReportingDashboardService().findStandardReports();
			assertEquals("There are the correct standard reports", expectedStandardReports, actualStandardReports);
		} else {
			assertTrue("There are no standard reports", actualStandardReports.isEmpty());
		}
	}

	/** {@inheritDoc} */
	@Override
	protected final C createBaseController() {
		return createBaseReportingDashboardController();
	}

	/**
	 * Creates a base reporting dashboard controller of the tpe to test.
	 * 
	 * @author IanBrown
	 * @return the base reporting dashboard controller.
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	protected abstract C createBaseReportingDashboardController();

	/**
	 * Fixes the report fields to make sure that their data is correct.
	 * 
	 * FIXME this shouldn't be necessary, but DBUnit appears to have problems with the user_field_name.
	 * 
	 * @author IanBrown
	 * @since Jan 31, 2012
	 * @version Mar 13, 2012
	 */
	protected void fixReportFields() {
		final ReportingDashboardDAO dao = getReportingDashboardService().getReportingDashboardDAO();
		final HibernateTemplate hibernateTemplate = dao.getHibernateTemplate();
		final List<ReportField> reportFields = hibernateTemplate.loadAll(ReportField.class);
		for (final ReportField reportField : reportFields) {
			if ((reportField.getId() == 5) && !"user_field".equals(reportField.getUserFieldName())) {
				reportField.setUserFieldName("user_field");
				hibernateTemplate.update(reportField);
			} else if ((reportField.getId() == 1) && !"voting_region_state".equals(reportField.getUserFieldName())) {
				reportField.setUserFieldName("voting_region_state");
				hibernateTemplate.update(reportField);
			} else if (reportField.getId() == 6) {
				reportField.setUserFieldName("flow_type");
			}
			hibernateTemplate.save(reportField);
		}
		final List<WizardResults> wizardResults = hibernateTemplate.loadAll(WizardResults.class);
		final GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(GregorianCalendar.DAY_OF_YEAR, -1);
		final Date date = calendar.getTime();
		for (final WizardResults wizardResult : wizardResults) {
			wizardResult.setCreationDate(date);
			wizardResult.setLastChangedDate(date);
			hibernateTemplate.save(wizardResult);
		}
		hibernateTemplate.flush();
	}

	/**
	 * Gets the overseas user DAO.
	 * 
	 * @author IanBrown
	 * @return the overseas user DAO.
	 * @since Jan 5, 2012
	 * @version Mar 7, 2012
	 */
	protected final OverseasUserDAO getOverseasUserDAO() {
		return overseasUserDAO;
	}

	/**
	 * Gets the reporting dashboard service.
	 * 
	 * @author IanBrown
	 * @return the reporting dashboard service.
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	protected final ReportingDashboardService getReportingDashboardService() {
		return reportingDashboardService;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setUpForBaseReportingDashboardController();
	}

	/**
	 * Sets up to test the specific type of reporting dashboard controller.
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
	}

	/**
	 * Tears down the set up for testing the specific type of reporting dashboard controller.
	 * 
	 * @author IanBrown
	 * @since Mar 7, 2012
	 * @version Mar 7, 2012
	 */
	protected abstract void tearDownForBaseReportingDashboardController();
}