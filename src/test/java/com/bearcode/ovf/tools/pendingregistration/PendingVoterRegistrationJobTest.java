/**
 * 
 */
package com.bearcode.ovf.tools.pendingregistration;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.xml.datatype.Duration;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.service.PendingVoterRegistrationService;

/**
 * Test for {@link PendingVoterRegistrationJob}.
 * 
 * @author IanBrown
 * 
 * @since Nov 26, 2012
 * @version Nov 26, 2012
 */
public final class PendingVoterRegistrationJobTest extends EasyMockSupport {

	/**
	 * the pending voter registration job to test.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistrationJob pendingVoterRegistrationJob;

	/**
	 * the pending voter registration service to use.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistrationService pendingVoterRegistrationService;

	private OvfPropertyService propertyService;

	/**
	 * Sets up to test the pending voter registration job.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Before
	public final void setUpPendingVoterRegistrationJob() {
		setPendingVoterRegistrationService(createMock("PendingVoterRegistrationService", PendingVoterRegistrationService.class));
		setPendingVoterRegistrationJob(createPendingVoterRegistrationJob());
		getPendingVoterRegistrationJob().setPendingVoterRegistrationService(getPendingVoterRegistrationService());
		setPropertyService(createMock("OvfPropertyService", OvfPropertyService.class));
	}

	/**
	 * Tears down the pending voter registration job after testing.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@After
	public final void tearDownPendingVoterRegistrationJob() {
		setPendingVoterRegistrationJob(null);
		setPendingVoterRegistrationService(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationJob#executeInternal(org.quartz.JobExecutionContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception if there is a problem timing out the pending voter registrations.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testExecuteInternalJobExecutionContext() throws Exception {
		final JobExecutionContext context = createJobExecutionContext();
		final Trigger trigger = createMock("Trigger", Trigger.class);
		EasyMock.expect(context.getTrigger()).andReturn(trigger).anyTimes();
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = createMock(
				"PendingVoterRegistrationConfiguration", PendingVoterRegistrationConfiguration.class);
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = Arrays
				.asList(pendingVoterRegistrationConfiguration);
		EasyMock.expect(getPendingVoterRegistrationService().getPendingVoterRegistrationConfigurations()).andReturn(
				pendingVoterRegistrationConfigurations);
		final String votingState = "VS";
		EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingState()).andReturn(votingState);
		final String votingRegion = "Voting Region";
		EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingRegion()).andReturn(votingRegion);
		final Duration timeout = createMock("Timeout", Duration.class);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getTimeout()).andReturn(timeout);
		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
				PendingVoterRegistration.class);
		final List<PendingVoterRegistration> pendingVoterRegistrations = Arrays.asList(pendingVoterRegistration);
		EasyMock.expect(getPendingVoterRegistrationService().findForConfiguration(pendingVoterRegistrationConfiguration, timeout, true))
		.andReturn(pendingVoterRegistrations);
		getPendingVoterRegistrationService().makeTimeout(pendingVoterRegistrations);
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getPendingVoterRegistrationJob().execute(context);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationJob#executeInternal(org.quartz.JobExecutionContext)}
	 * for the case where there are no pending voter registration configurations.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem scheduling the job execution.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testExecuteInternalJobExecutionContext_noPendingVoterRegistrationConfigurations() throws SchedulerException {
		final JobExecutionContext context = createJobExecutionContext();
		final Trigger trigger = createMock("Trigger", Trigger.class);
		EasyMock.expect(context.getTrigger()).andReturn(trigger).anyTimes();
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = null;
		EasyMock.expect(getPendingVoterRegistrationService().getPendingVoterRegistrationConfigurations()).andReturn(
				pendingVoterRegistrationConfigurations);
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getPendingVoterRegistrationJob().execute(context);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationJob#executeInternal(org.quartz.JobExecutionContext)}
	 * for the case where there are no pending voter registrations.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception if there is a problem decrypting the pending voter registrations.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testExecuteInternalJobExecutionContext_noPendingVoterRegistrations() throws Exception {
		final JobExecutionContext context = createJobExecutionContext();
		final Trigger trigger = createMock("Trigger", Trigger.class);
		EasyMock.expect(context.getTrigger()).andReturn(trigger).anyTimes();
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = createMock(
				"PendingVoterRegistrationConfiguration", PendingVoterRegistrationConfiguration.class);
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = Arrays
				.asList(pendingVoterRegistrationConfiguration);
		EasyMock.expect(getPendingVoterRegistrationService().getPendingVoterRegistrationConfigurations()).andReturn(
				pendingVoterRegistrationConfigurations);
		final String votingState = "VS";
		EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingState()).andReturn(votingState);
		final String votingRegion = "Voting Region";
		EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingRegion()).andReturn(votingRegion);
		final Duration timeout = createMock("Timeout", Duration.class);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getTimeout()).andReturn(timeout);
		final List<PendingVoterRegistration> pendingVoterRegistrations = new LinkedList<PendingVoterRegistration>();
		EasyMock.expect(getPendingVoterRegistrationService().findForConfiguration(pendingVoterRegistrationConfiguration, timeout, true))
		.andReturn(pendingVoterRegistrations);
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getPendingVoterRegistrationJob().execute(context);

		verifyAll();
	}

	/**
	 * Creates the job execution context.
	 * 
	 * @author IanBrown
	 * @return the job execution context.
	 * @throws SchedulerException
	 *             if there is a problem setting up the job execution context.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private JobExecutionContext createJobExecutionContext() throws SchedulerException {
		final JobExecutionContext context = createMock("JobExecutionContext", JobExecutionContext.class);
		final Scheduler scheduler = createMock("Scheduler", Scheduler.class);
		EasyMock.expect(context.getScheduler()).andReturn(scheduler).anyTimes();
		final SchedulerContext schedulerContext = new SchedulerContext();
		EasyMock.expect(scheduler.getContext()).andReturn(schedulerContext).anyTimes();
		final JobDataMap mergedJobDataMap = new JobDataMap();
		EasyMock.expect(context.getMergedJobDataMap()).andReturn(mergedJobDataMap).anyTimes();
		mergedJobDataMap.put("pendingVoterRegistrationService", getPendingVoterRegistrationService());
		mergedJobDataMap.put("propertyService", getPropertyService());
		return context;
	}

	/**
	 * Creates a pending voter registration job.
	 * 
	 * @author IanBrown
	 * @return the pending voter registration job.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistrationJob createPendingVoterRegistrationJob() {
		final PendingVoterRegistrationJob pendingVoterRegistrationJob = new PendingVoterRegistrationJob();
		return pendingVoterRegistrationJob;
	}

	/**
	 * Gets the pending voter registration job.
	 * 
	 * @author IanBrown
	 * @return the pending voter registration job.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistrationJob getPendingVoterRegistrationJob() {
		return pendingVoterRegistrationJob;
	}

	/**
	 * Gets the pending voter registration service.
	 * 
	 * @author IanBrown
	 * @return the pending voter registration service.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistrationService getPendingVoterRegistrationService() {
		return pendingVoterRegistrationService;
	}

	/**
	 * Sets the pending voter registration job.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistrationJob
	 *            the pending voter registration job to set.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private void setPendingVoterRegistrationJob(final PendingVoterRegistrationJob pendingVoterRegistrationJob) {
		this.pendingVoterRegistrationJob = pendingVoterRegistrationJob;
	}

	/**
	 * Sets the pending voter registration service.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistrationService
	 *            the pending voter registration service to set.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private void setPendingVoterRegistrationService(final PendingVoterRegistrationService pendingVoterRegistrationService) {
		this.pendingVoterRegistrationService = pendingVoterRegistrationService;
	}

	public OvfPropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(OvfPropertyService propertyService) {
		this.propertyService = propertyService;
	}
}
