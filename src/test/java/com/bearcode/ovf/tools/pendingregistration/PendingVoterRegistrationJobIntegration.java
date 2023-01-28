/**
 * 
 */
package com.bearcode.ovf.tools.pendingregistration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.Duration;

import com.bearcode.ovf.model.common.VoterType;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.model.common.VoterHistory;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAddress;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAnswer;
import com.bearcode.ovf.model.pendingregistration.PendingVoterName;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistrationStatus;
import com.bearcode.ovf.service.PendingVoterRegistrationService;

/**
 * Integration test for {@link PendingVoterRegistrationJob}.
 * 
 * @author IanBrown
 * 
 * @since Nov 26, 2012
 * @version Nov 26, 2012
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "../../actions/commons/applicationContext_test.xml",
		"PendingVoterRegistrationJobIntegration-context.xml" })
@DirtiesContext
public final class PendingVoterRegistrationJobIntegration extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the pending voter registration job.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistrationJob pendingVoterRegistrationJob;

	/**
	 * the pending voter registration service.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Autowired
	private PendingVoterRegistrationService pendingVoterRegistrationService;

	/**
	 * Gets the pending voter registration service.
	 * 
	 * @author IanBrown
	 * @return the pending voter registration service.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	public PendingVoterRegistrationService getPendingVoterRegistrationService() {
		return pendingVoterRegistrationService;
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
	public void setPendingVoterRegistrationService(final PendingVoterRegistrationService pendingVoterRegistrationService) {
		this.pendingVoterRegistrationService = pendingVoterRegistrationService;
	}

	/**
	 * Sets up the pending voter registration job to test.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Before
	public final void setUpPendingVoterRegistrationJob() {
		setPendingVoterRegistrationJob(new PendingVoterRegistrationJob());
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
	}

	/**
	 * Test method for {@link PendingVoterRegistrationJob}.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem testing the job.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testPendingVoterRegistrationJob() throws Exception {
		final JobExecutionContext context = EasyMock.createMock("Context", JobExecutionContext.class);
		final Trigger trigger = EasyMock.createMock("Trigger", Trigger.class);
		EasyMock.expect(context.getTrigger()).andReturn(trigger).anyTimes();
		final Scheduler scheduler = EasyMock.createMock("Scheduler", Scheduler.class);
		EasyMock.expect(context.getScheduler()).andReturn(scheduler).anyTimes();
		final SchedulerContext schedulerContext = new SchedulerContext();
		EasyMock.expect(scheduler.getContext()).andReturn(schedulerContext).anyTimes();
		final JobDataMap mergedJobDataMap = new JobDataMap();
		EasyMock.expect(context.getMergedJobDataMap()).andReturn(mergedJobDataMap).anyTimes();
		mergedJobDataMap.put("pendingVoterRegistrationService", getPendingVoterRegistrationService());
		final List<PendingVoterRegistration> pendingVoterRegistrations = createPendingVoterRegistrations();
		EasyMock.replay(context, trigger, scheduler);

		getPendingVoterRegistrationJob().execute(context);

		assertPendingVoterRegistrations(pendingVoterRegistrations);
		EasyMock.verify(context, trigger, scheduler);
	}

	/**
	 * Custom assertion to ensure that a new pending voter registration is properly left alone.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistration
	 *            the pending voter registration.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private void assertNewPendingVoterRegistration(final PendingVoterRegistration pendingVoterRegistration) {
		final PendingVoterRegistration actualPendingVoterRegistration = getPendingVoterRegistrationService().findById(
				pendingVoterRegistration.getId());
		assertNotNull("The pending voter registration is still saved", actualPendingVoterRegistration);

		final PendingVoterRegistrationStatus actualPendingVoterRegistrationStatus = getPendingVoterRegistrationService()
				.findStatusById(pendingVoterRegistration.getId());
		assertNull("There is no pending voter registration status", actualPendingVoterRegistrationStatus);
	}

	/**
	 * Custom assertion to ensure that an old pending voter registration is properly timed out.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistration
	 *            the pending voter registration.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private void assertOldPendingVoterRegistration(final PendingVoterRegistration pendingVoterRegistration) {
		final PendingVoterRegistration actualPendingVoterRegistration = getPendingVoterRegistrationService().findById(
				pendingVoterRegistration.getId());
		assertNull("The pending voter registration is no longer saved", actualPendingVoterRegistration);

		final PendingVoterRegistrationStatus actualPendingVoterRegistrationStatus = getPendingVoterRegistrationService()
				.findStatusById(pendingVoterRegistration.getId());
		assertNotNull("There is a pending voter registration status saved", actualPendingVoterRegistrationStatus);
		assertNotNull("The pending voter registration status has a completion date",
				actualPendingVoterRegistrationStatus.getCompletionDate());
		assertNull("The pending voter registration status indicates that the data was not downloaded",
				actualPendingVoterRegistrationStatus.getDownloadedBy());
	}

	/**
	 * Custom assertion to ensure that the pending voter registration is handled properly.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistration
	 *            the pending voter registration.
	 * @param oldDate
	 *            the oldest acceptable date.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private void assertPendingVoterRegistration(final PendingVoterRegistration pendingVoterRegistration, final Date oldDate) {
		if (pendingVoterRegistration.getCreatedDate().compareTo(oldDate) > 0) {
			assertNewPendingVoterRegistration(pendingVoterRegistration);
		} else {
			assertOldPendingVoterRegistration(pendingVoterRegistration);
		}
	}

	/**
	 * Custom assertion to ensure that the pending voter registrations are handled properly.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistrations
	 *            the pending voter registrations.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private void assertPendingVoterRegistrations(final List<PendingVoterRegistration> pendingVoterRegistrations) {
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = getPendingVoterRegistrationService()
				.getPendingVoterRegistrationConfigurations();
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = pendingVoterRegistrationConfigurations
				.get(0);
		final Duration timeout = pendingVoterRegistrationConfiguration.getTimeout();
		final Calendar calendar = new GregorianCalendar();
		final Duration passed = timeout.negate();
		passed.addTo(calendar);
		final Date oldDate = calendar.getTime();

		for (final PendingVoterRegistration pendingVoterRegistration : pendingVoterRegistrations) {
			assertPendingVoterRegistration(pendingVoterRegistration, oldDate);
		}
	}

	/**
	 * Creates a pending voter registration that is newer than the timeout period.
	 * 
	 * @author IanBrown
	 * @param votingState
	 *            the voting state.
	 * @param votingRegion
	 *            the voting region.
	 * @param timeout
	 *            the timeout period.
	 * @return the pending voter registration.
	 * @throws Exception
	 *             if there is a problem creating the pending voter registration.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistration createNewPendingVoterRegistration(final String votingState, final String votingRegion,
			final Duration timeout) throws Exception {
		final PendingVoterRegistration pendingVoterRegistration = createPendingVoterRegistration(votingState, votingRegion, "New");
		final Date createdDate = new Date();
		pendingVoterRegistration.setCreatedDate(createdDate);
		getPendingVoterRegistrationService().save(pendingVoterRegistration);
		return pendingVoterRegistration;
	}

	/**
	 * Creates a pending voter registration that is older than the timeout period. The pending voter registration is saved to the
	 * database.
	 * 
	 * @author IanBrown
	 * @param votingState
	 *            the voting state.
	 * @param votingRegion
	 *            the voting region.
	 * @param timeout
	 *            the timeout duration.
	 * @return the pending voter registration.
	 * @throws Exception
	 *             if there is a problem creating the pending voter registration.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistration createOldPendingVoterRegistration(final String votingState, final String votingRegion,
			final Duration timeout) throws Exception {
		final PendingVoterRegistration pendingVoterRegistration = createPendingVoterRegistration(votingState, votingRegion, "Old");
		final Calendar calendar = new GregorianCalendar();
		final Duration passed = timeout.negate();
		passed.addTo(calendar);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		final Date createdDate = calendar.getTime();
		pendingVoterRegistration.setCreatedDate(createdDate);
		getPendingVoterRegistrationService().save(pendingVoterRegistration);
		return pendingVoterRegistration;
	}

	/**
	 * Creates a pending voter address.
	 * 
	 * @author IanBrown
	 * @param registrationType
	 *            the pending voter registration type.
	 * @param addressType
	 *            the address type.
	 * @param votingState
	 *            the voting state.
	 * @return the pending voter address.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterAddress createPendingVoterAddress(final String registrationType, final String addressType,
			final String votingState) {
		final PendingVoterAddress pendingVoterAddress = new PendingVoterAddress();
		pendingVoterAddress.setCity("City of " + registrationType + " " + addressType);
		pendingVoterAddress.setCountry("USA");
		pendingVoterAddress.setDescription("Description of " + registrationType + " " + addressType);
		pendingVoterAddress.setPostalCode(Integer.toString((registrationType.hashCode() + addressType.hashCode()) % 100000));
		pendingVoterAddress.setStateOrRegion(votingState);
		pendingVoterAddress.setStreet1("1 " + registrationType + " " + addressType + " Street");
		pendingVoterAddress.setStreet2("Unit " + (registrationType.hashCode() + addressType.hashCode()) % 10);
		return pendingVoterAddress;
	}

	/**
	 * Creates pending voter answers.
	 * 
	 * @author IanBrown
	 * @param registrationType
	 *            the pending voter registration type.
	 * @return the answers.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private List<PendingVoterAnswer> createPendingVoterAnswers(final String registrationType) {
		final PendingVoterAnswer answer = new PendingVoterAnswer();
		answer.setQuestion("Question");
		answer.setAnswer(registrationType);
		final List<PendingVoterAnswer> answers = Arrays.asList(answer);
		return answers;
	}

	/**
	 * Creates a pending voter name.
	 * 
	 * @author IanBrown
	 * @param registrationType
	 *            the pending voter registration type.
	 * @param nameType
	 *            the type of name.
	 * @return the pending voter name.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterName createPendingVoterName(final String registrationType, final String nameType) {
		final PendingVoterName pendingVoterName = new PendingVoterName();
		pendingVoterName.setFirstName(registrationType);
		pendingVoterName.setLastName(nameType);
		pendingVoterName.setMiddleName(registrationType.substring(0, 1) + ".");
		pendingVoterName.setSuffix(nameType.substring(0, 2));
		pendingVoterName.setTitle(registrationType.substring(0, 2));
		return pendingVoterName;
	}

	/**
	 * Creates a pending voter registration.
	 * 
	 * @author IanBrown
	 * @param votingState
	 *            the voting state.
	 * @param votingRegion
	 *            the voting region.
	 * @param registrationType
	 *            the pending voter registration type.
	 * @return the pending voter registration.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistration createPendingVoterRegistration(final String votingState, final String votingRegion,
			final String registrationType) {
		final PendingVoterRegistration pendingVoterRegistration = new PendingVoterRegistration();
		pendingVoterRegistration.setAlternateEmailAddress("alternate@email.address." + registrationType);
		pendingVoterRegistration.setAlternatePhoneNumber(Integer.toString(registrationType.hashCode() + 1));
		pendingVoterRegistration.setAnswers(createPendingVoterAnswers(registrationType));
		pendingVoterRegistration.setBirthDay(PendingVoterRegistration.BIRTH_DAY_FORMAT.format(new Date()));
		pendingVoterRegistration.setCurrentAddress(createPendingVoterAddress(registrationType, "Current", votingState));
		pendingVoterRegistration.setEmailAddress("primary@email.address." + registrationType);
		pendingVoterRegistration.setFacePrefix("faces/" + (votingRegion == null ? votingState : votingRegion));
		pendingVoterRegistration.setForwardingAddress(createPendingVoterAddress(registrationType, "Forwarding", votingState));
		pendingVoterRegistration.setGender(registrationType.hashCode() % 2 == 0 ? "Male" : "Female");
		pendingVoterRegistration.setName(createPendingVoterName(registrationType, "Current"));
		pendingVoterRegistration.setPhoneNumber(Integer.toString(registrationType.hashCode()));
		pendingVoterRegistration.setPreviousAddress(createPendingVoterAddress(registrationType, "Previous", votingState));
		pendingVoterRegistration.setPreviousName(createPendingVoterName(registrationType, "Previous"));
		pendingVoterRegistration.setVoterHistory(VoterHistory.values()[registrationType.hashCode() % VoterHistory.values().length]
				.name());
		pendingVoterRegistration.setVoterType( VoterType.values()[registrationType.hashCode() % VoterType.values().length].name());
		pendingVoterRegistration.setVotingAddress(createPendingVoterAddress(registrationType, "Voting", votingState));
		pendingVoterRegistration.setVotingRegion(votingRegion);
		pendingVoterRegistration.setVotingState(votingState);
		return pendingVoterRegistration;
	}

	/**
	 * Creates the pending voter registrations for the test. There are the following registrations created:
	 * <ol>
	 * <li>One that is old and should be timed out, and</li>
	 * <li>One that is new and shouldn't be touched.</li>
	 * </ol>
	 * 
	 * @author IanBrown
	 * @return the pending voter registrations.
	 * @throws Exception
	 *             if there is a problem creating the voter registrations.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private List<PendingVoterRegistration> createPendingVoterRegistrations() throws Exception {
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = getPendingVoterRegistrationService()
				.getPendingVoterRegistrationConfigurations();
		assertNotNull("There are pending voter registration configurations", pendingVoterRegistrationConfigurations);
		assertEquals("There is one pending voter registration configuration", 1, pendingVoterRegistrationConfigurations.size());
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = pendingVoterRegistrationConfigurations
				.get(0);
		final String votingState = pendingVoterRegistrationConfiguration.getVotingState();
		final String votingRegion = pendingVoterRegistrationConfiguration.getVotingRegion();
		final Duration timeout = pendingVoterRegistrationConfiguration.getTimeout();

		final List<PendingVoterRegistration> pendingVoterRegistrations = Arrays.asList(
				createOldPendingVoterRegistration(votingState, votingRegion, timeout),
				createNewPendingVoterRegistration(votingState, votingRegion, timeout));
		return pendingVoterRegistrations;
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
}
