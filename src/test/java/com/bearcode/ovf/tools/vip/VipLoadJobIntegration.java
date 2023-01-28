/**
 * 
 */
package com.bearcode.ovf.tools.vip;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.model.vip.VipBallot;
import com.bearcode.ovf.model.vip.VipBallotCandidate;
import com.bearcode.ovf.model.vip.VipBallotResponse;
import com.bearcode.ovf.model.vip.VipCandidate;
import com.bearcode.ovf.model.vip.VipContest;
import com.bearcode.ovf.model.vip.VipCustomBallot;
import com.bearcode.ovf.model.vip.VipCustomBallotResponse;
import com.bearcode.ovf.model.vip.VipDetailAddress;
import com.bearcode.ovf.model.vip.VipElection;
import com.bearcode.ovf.model.vip.VipElectoralDistrict;
import com.bearcode.ovf.model.vip.VipLocality;
import com.bearcode.ovf.model.vip.VipPrecinct;
import com.bearcode.ovf.model.vip.VipPrecinctSplit;
import com.bearcode.ovf.model.vip.VipReferendum;
import com.bearcode.ovf.model.vip.VipReferendumBallotResponse;
import com.bearcode.ovf.model.vip.VipSource;
import com.bearcode.ovf.model.vip.VipStreetSegment;
import com.bearcode.ovf.service.VipService;

/**
 * Integration test for {@link VipLoadJob}.
 * 
 * @author IanBrown
 * 
 * @since Jun 25, 2012
 * @version May 12, 2013
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "../../actions/commons/applicationContext_test.xml", "VipLoadJobIntegration-context.xml" })
@DirtiesContext
public final class VipLoadJobIntegration extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the VIP load job to test.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipLoadJob vipLoadJob;

	/**
	 * the VIP service to use.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@Autowired
	private VipService vipService;

	/**
	 * the VIP load job valet to use (mocked to provide local input rather than use the Web).
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipLoadJobValet valet;

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public VipLoadJobValet getValet() {
		return valet;
	}

	/**
	 * Gets the vipLoadJob.
	 * 
	 * @author IanBrown
	 * @return the vipLoadJob.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public VipLoadJob getVipLoadJob() {
		return vipLoadJob;
	}

	/**
	 * Sets up the VIP load job to test.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@Before
	public final void setUpVipLoadJob() {
		setValet(EasyMock.createMock("Valet", VipLoadJobValet.class));
		setVipLoadJob(new VipLoadJob());
		getVipLoadJob().setValet(getValet());
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void setValet(final VipLoadJobValet valet) {
		this.valet = valet;
	}

	/**
	 * Sets the vipLoadJob.
	 * 
	 * @author IanBrown
	 * @param vipLoadJob
	 *            the vipLoadJob to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void setVipLoadJob(final VipLoadJob vipLoadJob) {
		this.vipLoadJob = vipLoadJob;
	}

	/**
	 * Tears down the VIP load job after testing it.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@After
	public final void tearDownVipLoadJob() {
		setVipLoadJob(null);
		setValet(null);
	}

	/**
	 * Test method for {@link org.springframework.scheduling.quartz.QuartzJobBean#execute(org.quartz.JobExecutionContext)}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem with the scheduler.
	 * @throws IOException
	 *             if there is a problem setting up the valet.
	 * @since Jun 25, 2012
	 * @version May 12, 2013
	 */
	@Test
	@OVFDBUnitUseData
	public final void testExecute() throws SchedulerException, IOException {
		final JobExecutionContext context = EasyMock.createMock("Context", JobExecutionContext.class);
		JobDetail jobDetail = EasyMock.createMock("JobDetail", JobDetail.class);
		EasyMock.expect(context.getJobDetail()).andReturn(jobDetail).anyTimes();
		EasyMock.expect(jobDetail.getName()).andReturn("VipLoadJobIntegration").anyTimes();
		final Scheduler scheduler = EasyMock.createMock("Scheduler", Scheduler.class);
		EasyMock.expect(context.getScheduler()).andReturn(scheduler).anyTimes();
		final SchedulerContext schedulerContext = new SchedulerContext();
		EasyMock.expect(scheduler.getContext()).andReturn(schedulerContext).anyTimes();
		final JobDataMap mergedJobDataMap = new JobDataMap();
		EasyMock.expect(context.getMergedJobDataMap()).andReturn(mergedJobDataMap).anyTimes();
		final String vipJobTarget = "src/test/resources/" + getClass().getPackage().getName().replace(".", "/") + "/vip.xml";
		mergedJobDataMap.put("vipJobTarget", vipJobTarget);
		mergedJobDataMap.put("vipService", getVipService());
		final File vipJobFile = new File(vipJobTarget);
		final FileInputStream fis = new FileInputStream(vipJobFile);
		EasyMock.expect(getValet().acquireInputStream(vipJobTarget)).andReturn(fis);
		EasyMock.expect(getValet().acquireLastModified(vipJobTarget)).andReturn(new Date());
		EasyMock.expect(getValet().loadVipObject(fis)).andDelegateTo(new VipLoadJob.VipLoadJobValetImpl());
		EasyMock.replay(context, jobDetail, scheduler, getValet());

		getVipLoadJob().execute(context);

		final VipSource source = getVipService().findLatestSource();
		assertNotNull("There is a latest source", source);
		final VipElection election = assertElection(source);
		assertPrecincts(source);
		assertPrecinctSplits(source);
		assertContests(source, election);
		assertStreetSegments(source);
		EasyMock.verify(context, jobDetail, scheduler, getValet());
	}


	/**
	 * Custom assertion to ensure that the ballot is correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param ballot
	 *            the ballot.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertBallot(final VipSource source, final VipBallot ballot) {
		assertSame("The source of the ballot is correct", source, ballot.getSource());

		boolean foundReason = false;
		final List<VipBallotCandidate> candidates = ballot.getCandidates();
		if (candidates != null && !candidates.isEmpty()) {
			foundReason = true;
			assertBallotCandidates(source, ballot, candidates);
		}

		final VipReferendum referendum = ballot.getReferendum();
		if (referendum != null) {
			foundReason = true;
			assertReferendum(source, referendum);
		}

		final VipCustomBallot customBallot = ballot.getCustomBallot();
		if (customBallot != null) {
			foundReason = true;
			assertCustomBallot(source, customBallot);
		}

		assertTrue("There is a reason for the ballot", foundReason);
	}

	/**
	 * Custom assertion to ensure that the ballot candidate is correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param ballot
	 *            the ballot.
	 * @param ballotCandidate
	 *            the ballot candidate.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertBallotCandidate(final VipSource source, final VipBallot ballot, final VipBallotCandidate ballotCandidate) {
		assertSame("The ballot for the ballot candidate is correct", ballot, ballotCandidate.getBallot());
		assertCandidate(source, ballotCandidate.getCandidate());
	}

	/**
	 * Custom assertion to ensure that the ballot candidates are correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param ballot
	 *            the ballot.
	 * @param ballotCandidates
	 *            the ballot candidates.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertBallotCandidates(final VipSource source, final VipBallot ballot,
			final List<VipBallotCandidate> ballotCandidates) {
		assertFalse("There are candidates", ballotCandidates.isEmpty());

		for (final VipBallotCandidate ballotCandidate : ballotCandidates) {
			assertBallotCandidate(source, ballot, ballotCandidate);
		}
	}

	/**
	 * Custom assertion to ensure that ballot response is correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param ballotResponse
	 *            the ballot response.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertBallotResponse(final VipSource source, final VipBallotResponse ballotResponse) {
		assertSame("The ballot response source is set", source, ballotResponse.getSource());
	}

	/**
	 * Custom assertion to ensure that the candidate is correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param candidate
	 *            the candidate.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertCandidate(final VipSource source, final VipCandidate candidate) {
		assertSame("The source of the candidate is correct", source, candidate.getSource());
	}

	/**
	 * Custom assertion to ensure that the contest is valid.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param election
	 *            the election.
	 * @param contest
	 *            the contest.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertContest(final VipSource source, final VipElection election, final VipContest contest) {
		assertSame("The source of the contest is correct", source, contest.getSource());
		assertSame("The election for the contest is correct", election, contest.getElection());
		assertElectoralDistrict(source, contest.getElectoralDistrict());
		assertBallot(source, contest.getBallot());
	}

	/**
	 * Custom assertion to ensure that the contests are correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param election
	 *            the election.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertContests(final VipSource source, final VipElection election) {
		final List<VipContest> contests = getVipService().findContestsBySourceAndElection(source, election);
		assertFalse("There are contests", contests.isEmpty());

		for (final VipContest contest : contests) {
			assertContest(source, election, contest);
		}
	}

	/**
	 * Custom assertion to ensure that the custom ballot is correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param customBallot
	 *            the custom ballot.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertCustomBallot(final VipSource source, final VipCustomBallot customBallot) {
		assertSame("The custom ballot source is set", source, customBallot.getSource());

		for (final VipCustomBallotResponse customBallotResponse : customBallot.getBallotResponses()) {
			assertSame("The custom ballot response custom ballot is set", customBallot, customBallotResponse.getCustomBallot());
			assertBallotResponse(source, customBallotResponse.getBallotResponse());
		}
	}

	/**
	 * Custom assertion to ensure that an detail address is correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param detailAddress
	 *            the detail address.
	 * @since Jun 29, 2012
	 * @version Jul 5, 2012
	 */
	private void assertDetailAddress(final VipSource source, final VipDetailAddress detailAddress) {
		assertNotNull("The detail address has a street name", detailAddress.getStreetName());
	}

	/**
	 * Custom assertion to ensure that there is an election with information stored.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @return the election.
	 * @since Jun 25, 2012
	 * @version Jun 28, 2012
	 */
	private VipElection assertElection(final VipSource source) {
		final VipElection election = getVipService().findElectionBySource(source);

		assertNotNull("There is an election for the source", election);
		assertSame("The election belongs to the source", source, election.getSource());

		return election;
	}

	/**
	 * Custom assertion to ensure that the electoral district makes sense.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param electoralDistrict
	 *            the electoral district.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertElectoralDistrict(final VipSource source, final VipElectoralDistrict electoralDistrict) {
		assertSame("The source of the electoral district is correct", source, electoralDistrict.getSource());
	}

	/**
	 * Custom assertion to ensure that the electoral districts make sense.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param electoralDistricts
	 *            the electoral districts.
	 * @since Jun 28, 2012
	 * @version Aug 9, 2012
	 */
	private void assertElectoralDistricts(final VipSource source, final List<VipElectoralDistrict> electoralDistricts) {
		assertFalse("There are electoral districts", electoralDistricts.isEmpty());

		for (final VipElectoralDistrict electoralDistrict : electoralDistricts) {
			assertElectoralDistrict(source, electoralDistrict);
		}
	}

	/**
	 * Custom assertion to ensure that the locality makes sense.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param locality
	 *            the locality.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertLocality(final VipSource source, final VipLocality locality) {
		assertSame("The source of the locality is correct", source, locality.getSource());
	}

	/**
	 * Custom assertion to ensure that the precinct makes sense.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @param precinct
	 *            the precinct.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertPrecinct(final VipSource source, final VipPrecinct precinct) {
		assertSame("The precinct belongs to the source", source, precinct.getSource());
		assertElectoralDistricts(source, precinct.getElectoralDistricts());
		assertLocality(source, precinct.getLocality());
		assertNotNull("The precinct has a name", precinct.getName());
	}

	/**
	 * Custom assertion to ensure that there are precincts for the source.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertPrecincts(final VipSource source) {
		final List<VipPrecinct> precincts = getVipService().findPrecinctsBySource(source);
		assertFalse("There are precincts", precincts.isEmpty());

		for (final VipPrecinct precinct : precincts) {
			assertPrecinct(source, precinct);
		}
	}

	/**
	 * Custom assertion to ensure that the precinct split is correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param precinctSplit
	 *            the precinct split.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private void assertPrecinctSplit(final VipSource source, final VipPrecinctSplit precinctSplit) {
		assertSame("The precinct split belongs to the source", source, precinctSplit.getSource());
		assertElectoralDistricts(source, precinctSplit.getElectoralDistricts());
		assertPrecinct(source, precinctSplit.getPrecinct());
		assertNotNull("The precinct split has a name", precinctSplit.getName());
	}

	/**
	 * Custom assertion to ensure that the precinct splits are correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private void assertPrecinctSplits(final VipSource source) {
		final List<VipPrecinctSplit> precinctSplits = getVipService().findPrecinctSplitsBySource(source);
		assertFalse("There are precinct splits", precinctSplits.isEmpty());

		for (final VipPrecinctSplit precinctSplit : precinctSplits) {
			assertPrecinctSplit(source, precinctSplit);
		}
	}

	/**
	 * Custom assertion to ensure that the referendum is correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param referendum
	 *            the referendum.
	 * @since Jun 28, 2012
	 * @version Jun 29, 2012
	 */
	private void assertReferendum(final VipSource source, final VipReferendum referendum) {
		assertSame("The source of the referendum is correct", source, referendum.getSource());
		assertNotNull("The referendum has a title", referendum.getTitle());
		assertNotNull("The referendum has a brief", referendum.getBrief());
		assertNotNull("The referendum has text", referendum.getText());

		for (final VipReferendumBallotResponse referendumBallotResponse : referendum.getBallotResponses()) {
			assertSame("The referndum ballot response referendum is set", referendum, referendumBallotResponse.getReferendum());
			assertBallotResponse(source, referendumBallotResponse.getBallotResponse());
		}
	}

	/**
	 * Custom assertion to ensure that the street segment is correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @param streetSegment
	 *            the street segment.
	 * @since Jun 29, 2012
	 * @version Jun 29, 2012
	 */
	private void assertStreetSegment(final VipSource source, final VipStreetSegment streetSegment) {
		assertSame("The street segment source is set", source, streetSegment.getSource());
		assertPrecinct(source, streetSegment.getPrecinct());
		final VipPrecinctSplit precinctSplit = streetSegment.getPrecinctSplit();
		if (precinctSplit != null) {
			assertPrecinctSplit(source, precinctSplit);
		}
		assertDetailAddress(source, streetSegment.getNonHouseAddress());
	}

	/**
	 * Custom assertion to ensure that the street segments are correct.
	 * 
	 * @author IanBrown
	 * @param source
	 *            the source of the data.
	 * @since Jun 28, 2012
	 * @version Jun 28, 2012
	 */
	private void assertStreetSegments(final VipSource source) {
		final List<VipStreetSegment> streetSegments = getVipService().findStreetSegmentsBySource(source);
		assertFalse("There are street segments", streetSegments.isEmpty());

		for (final VipStreetSegment streetSegment : streetSegments) {
			assertStreetSegment(source, streetSegment);
		}
	}

	/**
	 * Gets the vipService.
	 * 
	 * @author IanBrown
	 * @return the vipService.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}
}
