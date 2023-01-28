/**
 * 
 */
package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.PendingVoterRegistrationDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.pendingregistration.PendingVoterName;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistrationStatus;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationCipherTest;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationConfiguration;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.Duration;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test for {@link PendingVoterRegistrationService}.
 * 
 * @author IanBrown
 * 
 * @since Nov 5, 2012
 * @version Nov 29, 2012
 */
public final class PendingVoterRegistrationServiceTest extends EasyMockSupport {

	/**
	 * the encryption service.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private EncryptionService encryptionService;

	/**
	 * the pending voter registration DAO.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private PendingVoterRegistrationDAO pendingVoterRegistrationDAO;

	/**
	 * the pending voter registration service to test.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private PendingVoterRegistrationService pendingVoterRegistrationService;


    private PendingVoterRegistrationCipherTest pendingVoterRegistrationCipher;

	/**
	 * Sets up the pending voter registration service to test.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	@Before
	public final void setUpPendingVoterRegistrationService() {
		setPendingVoterRegistrationDAO(createMock("PendingVoterRegistrationDAO", PendingVoterRegistrationDAO.class));
		setPendingVoterRegistrationService(createPendingVoterRegistrationService());

		getPendingVoterRegistrationService().setPendingVoterRegistrationDAO(getPendingVoterRegistrationDAO());
	}

	/**
	 * Tears down the pending voter registration service after testing.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	@After
	public final void tearDownPendingVoterRegistrationService() {
		setPendingVoterRegistrationService(null);
		setPendingVoterRegistrationDAO(null);
		setEncryptionService(null);
	}


	/**
	 * Test method for {@link com.bearcode.ovf.service.PendingVoterRegistrationService#delete(PendingVoterRegistration))}.
	 * 
	 * @author IanBrown
	 * @since Nov 15, 2012
	 * @version Nov 15, 2012
	 */
	@Test
	public final void testDelete() {
		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
				PendingVoterRegistration.class);
		getPendingVoterRegistrationDAO().makeTransient(pendingVoterRegistration);
		replayAll();

		getPendingVoterRegistrationService().delete(pendingVoterRegistration);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findById(Long)}.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindById() {
		final Long id = 2989l;
		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
				PendingVoterRegistration.class);
		EasyMock.expect(getPendingVoterRegistrationDAO().findById(id)).andReturn(pendingVoterRegistration);
		replayAll();

		final PendingVoterRegistration actualPendingVoterRegistration = getPendingVoterRegistrationService().findById(id);

		assertSame("The pending voter registration is returned", pendingVoterRegistration, actualPendingVoterRegistration);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findForConfiguration(com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationConfiguration)}
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem with the decryption.
	 * @since Nov 5, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindForVotingStateAndRegionStringString() throws Exception {
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
        final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
           				PendingVoterRegistration.class);
        final PendingVoterRegistrationConfiguration configuration = createMock( "PendingVoterRegistrationConfiguration",
                PendingVoterRegistrationConfiguration.class );
        EasyMock.expect( configuration.isRequireShs() ).andReturn( false ).anyTimes();
        EasyMock.expect( configuration.getVotingState() ).andReturn( votingState ).anyTimes();
        EasyMock.expect( configuration.getVotingRegion() ).andReturn( votingRegion ).anyTimes();
		final List<PendingVoterRegistration> pendingVoterRegistrations = Arrays.asList(pendingVoterRegistration);
		EasyMock.expect(getPendingVoterRegistrationDAO().findForConfiguration(configuration, null, false )).andReturn(
				pendingVoterRegistrations);
		replayAll();

		final List<PendingVoterRegistration> actualPendingVoterRegistrations = getPendingVoterRegistrationService()
				.findForConfiguration(configuration);

		assertSame("The pending voter registrations are returned", pendingVoterRegistrations, actualPendingVoterRegistrations);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findForConfiguration(com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationConfiguration, javax.xml.datatype.Duration)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem with the decryption.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindForVotingStateAndRegionStringStringDuration() throws Exception {
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		final Duration timeout = createMock("Timeout", Duration.class);
        final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
           				PendingVoterRegistration.class);
        final List<PendingVoterRegistration> pendingVoterRegistrations = Arrays.asList(pendingVoterRegistration);
        final PendingVoterRegistrationConfiguration configuration = createMock( "PendingVoterRegistrationConfiguration",
                PendingVoterRegistrationConfiguration.class );
        EasyMock.expect( configuration.isRequireShs() ).andReturn( false ).anyTimes();
        EasyMock.expect( configuration.getVotingState() ).andReturn( votingState ).anyTimes();
        EasyMock.expect( configuration.getVotingRegion() ).andReturn( votingRegion ).anyTimes();
		EasyMock.expect(getPendingVoterRegistrationDAO().findForConfiguration(configuration, timeout, false))
		.andReturn(pendingVoterRegistrations);
		replayAll();

		final List<PendingVoterRegistration> actualPendingVoterRegistrations = getPendingVoterRegistrationService()
				.findForConfiguration(configuration, timeout);

		assertSame("The pending voter registrations are returned", pendingVoterRegistrations, actualPendingVoterRegistrations);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findPendingVoterRegistrationConfiguration(boolean, boolean, FaceConfig, FaceConfig, String, String)}
	 * .
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 28, 2012
	 */
	@Test
	public final void testFindPendingVoterConfiguration() {
		final boolean administrator = false;
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String facePrefix = "Face Prefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(facePrefix);
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = createMock(
				"PendingVoterRegistrationConfiguration", PendingVoterRegistrationConfiguration.class);
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = Arrays
				.asList(pendingVoterRegistrationConfiguration);
		getPendingVoterRegistrationService().setPendingVoterRegistrationConfigurations(pendingVoterRegistrationConfigurations);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isEnabled()).andReturn(true);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isRequireShs()).andReturn(true);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getFacePrefix()).andReturn(facePrefix);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingState()).andReturn(votingState);
		//EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingRegion()).andReturn(votingRegion);
		replayAll();

		final PendingVoterRegistrationConfiguration actualPendingVoterRegistrationConfiguration = getPendingVoterRegistrationService()
				.findPendingVoterRegistrationConfiguration(administrator, false, faceConfig, faceConfig, votingState, votingRegion);

		assertSame("The pending voter registration configuration is returned", pendingVoterRegistrationConfiguration,
				actualPendingVoterRegistrationConfiguration);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findPendingVoterRegistrationConfiguration(boolean, boolean, FaceConfig, FaceConfig, String, String)}
	 * for an administrator.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 28, 2012
	 */
	@Test
	public final void testFindPendingVoterConfiguration_administrator() {
		final boolean administrator = true;
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String facePrefix = "Face Prefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(facePrefix);
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = createMock(
				"PendingVoterRegistrationConfiguration", PendingVoterRegistrationConfiguration.class);
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = Arrays
				.asList(pendingVoterRegistrationConfiguration);
		getPendingVoterRegistrationService().setPendingVoterRegistrationConfigurations(pendingVoterRegistrationConfigurations);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isEnabled()).andReturn(true);
        EasyMock.expect(pendingVoterRegistrationConfiguration.isRequireShs()).andReturn(false);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingState()).andReturn(votingState);
		//EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingRegion()).andReturn(votingRegion);
		replayAll();

		final PendingVoterRegistrationConfiguration actualPendingVoterRegistrationConfiguration = getPendingVoterRegistrationService()
				.findPendingVoterRegistrationConfiguration(administrator, false, faceConfig, faceConfig, votingState, votingRegion);

		assertSame("The pending voter registration configuration is returned", pendingVoterRegistrationConfiguration,
				actualPendingVoterRegistrationConfiguration);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findPendingVoterRegistrationConfiguration(boolean, boolean, FaceConfig, FaceConfig, String, String)}
	 * for the case where there are no pending voter registration configurations.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 14, 2012
	 */
	@Test
	public final void testFindPendingVoterConfiguration_noPendingVoterConfigurations() {
		final boolean administrator = false;
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		replayAll();

		final PendingVoterRegistrationConfiguration actualPendingVoterRegistrationConfiguration = getPendingVoterRegistrationService()
				.findPendingVoterRegistrationConfiguration(administrator, false, faceConfig, faceConfig, votingState, votingRegion);

		assertNull("No pending voter registration configuration is returned", actualPendingVoterRegistrationConfiguration);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findPendingVoterRegistrationConfiguration(boolean, boolean, FaceConfig, FaceConfig, String, String)}
	 * for the case where the SHS is not required.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 28, 2012
	 */
	@Test
	public final void testFindPendingVoterConfiguration_shsNotRequired() {
		final boolean administrator = false;
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String facePrefix = "Face Prefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(facePrefix);
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = createMock(
				"PendingVoterRegistrationConfiguration", PendingVoterRegistrationConfiguration.class);
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = Arrays
				.asList(pendingVoterRegistrationConfiguration);
		getPendingVoterRegistrationService().setPendingVoterRegistrationConfigurations(pendingVoterRegistrationConfigurations);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isEnabled()).andReturn(true);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isRequireShs()).andReturn(false);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingState()).andReturn(votingState);
		//EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingRegion()).andReturn(votingRegion);
		replayAll();

		final PendingVoterRegistrationConfiguration actualPendingVoterRegistrationConfiguration = getPendingVoterRegistrationService()
				.findPendingVoterRegistrationConfiguration(administrator, false, faceConfig, faceConfig, votingState, votingRegion);

		assertSame("The pending voter registration configuration is returned", pendingVoterRegistrationConfiguration,
				actualPendingVoterRegistrationConfiguration);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findPendingVoterRegistrationConfiguration(boolean, boolean, FaceConfig, FaceConfig, String, String)}
	 * for the case where the SHS is not the correct one.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 28, 2012
	 */
	@Test
	public final void testFindPendingVoterConfiguration_wrongShs() {
		final boolean administrator = false;
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String facePrefix = "Face Prefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(facePrefix);
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = createMock(
				"PendingVoterRegistrationConfiguration", PendingVoterRegistrationConfiguration.class);
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = Arrays
				.asList(pendingVoterRegistrationConfiguration);
		getPendingVoterRegistrationService().setPendingVoterRegistrationConfigurations(pendingVoterRegistrationConfigurations);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isEnabled()).andReturn(true);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isRequireShs()).andReturn(true);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getFacePrefix()).andReturn("Different Face Prefix");
		replayAll();

		final PendingVoterRegistrationConfiguration actualPendingVoterRegistrationConfiguration = getPendingVoterRegistrationService()
				.findPendingVoterRegistrationConfiguration(administrator, false, faceConfig, faceConfig, votingState, votingRegion);

		assertNull("No pending voter registration configuration is returned", actualPendingVoterRegistrationConfiguration);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findPendingVoterRegistrationConfiguration(boolean, boolean, FaceConfig, FaceConfig, String, String)}
	 * for the case where the voting region is wrong.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 28, 2012
	 */
/*
	@Test
	public final void testFindPendingVoterConfiguration_wrongVotingRegion() {
		final boolean administrator = false;
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String facePrefix = "Face Prefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(facePrefix);
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = createMock(
				"PendingVoterRegistrationConfiguration", PendingVoterRegistrationConfiguration.class);
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = Arrays
				.asList(pendingVoterRegistrationConfiguration);
		getPendingVoterRegistrationService().setPendingVoterRegistrationConfigurations(pendingVoterRegistrationConfigurations);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isEnabled()).andReturn(true);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isRequireShs()).andReturn(true);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getFacePrefix()).andReturn(facePrefix);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingState()).andReturn(votingState);
		//EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingRegion()).andReturn("Different Voting Region");
		replayAll();

		final PendingVoterRegistrationConfiguration actualPendingVoterRegistrationConfiguration = getPendingVoterRegistrationService()
				.findPendingVoterRegistrationConfiguration(administrator, false, faceConfig, faceConfig, votingState, votingRegion);

		assertNull("No pending voter registration configuration is returned", actualPendingVoterRegistrationConfiguration);
		verifyAll();
	}
*/

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findPendingVoterRegistrationConfiguration(boolean, boolean, FaceConfig, FaceConfig, String, String)}
	 * for the case where the voting state is wrong.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 28, 2012
	 */
	@Test
	public final void testFindPendingVoterConfiguration_wrongVotingState() {
		final boolean administrator = false;
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String facePrefix = "Face Prefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(facePrefix);
		final String votingState = "VS";
		final String votingRegion = "Voting Region";
		final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = createMock(
				"PendingVoterRegistrationConfiguration", PendingVoterRegistrationConfiguration.class);
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = Arrays
				.asList(pendingVoterRegistrationConfiguration);
		getPendingVoterRegistrationService().setPendingVoterRegistrationConfigurations(pendingVoterRegistrationConfigurations);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isEnabled()).andReturn(true);
		EasyMock.expect(pendingVoterRegistrationConfiguration.isRequireShs()).andReturn(true);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getFacePrefix()).andReturn(facePrefix);
		EasyMock.expect(pendingVoterRegistrationConfiguration.getVotingState()).andReturn("DS");
		replayAll();

		final PendingVoterRegistrationConfiguration actualPendingVoterRegistrationConfiguration = getPendingVoterRegistrationService()
				.findPendingVoterRegistrationConfiguration(administrator, false, faceConfig, faceConfig, votingState, votingRegion);

		assertNull("No pending voter registration configuration is returned", actualPendingVoterRegistrationConfiguration);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.PendingVoterRegistrationService#findStatusById(Long)}.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	@Test
	public final void testFindStatusById() {
		final Long id = 90297l;
		final PendingVoterRegistrationStatus pendingVoterRegistrationStatus = createMock("PendingVoterRegistrationStatus",
				PendingVoterRegistrationStatus.class);
		EasyMock.expect(getPendingVoterRegistrationDAO().findStatusById(id)).andReturn(pendingVoterRegistrationStatus);
		replayAll();

		final PendingVoterRegistrationStatus actualPendingVoterRegistrationStatus = getPendingVoterRegistrationService()
				.findStatusById(id);

		assertSame("The pending voter registration status is returned", pendingVoterRegistrationStatus,
				actualPendingVoterRegistrationStatus);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#makeComplete(com.bearcode.ovf.model.common.OverseasUser, java.util.List)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Nov 5, 2012
	 * @version Nov 15, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testMakeComplete() {
		final long id = 12987l;
		final PendingVoterName name = createMock("Name", PendingVoterName.class);
		final OverseasUser user = createMock("DownloadedBy", OverseasUser.class);
		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
				PendingVoterRegistration.class);
		final List<PendingVoterRegistration> pendingVoterRegistrations = Arrays.asList(pendingVoterRegistration);
		EasyMock.expect(pendingVoterRegistration.getId()).andReturn(id);
		EasyMock.expect(pendingVoterRegistration.getName()).andReturn(name);
		EasyMock.expect(name.getEncrypted()).andReturn(null);
		final String title = "Title";
		EasyMock.expect(name.getTitle()).andReturn(title);
		final String firstName = "First";
		EasyMock.expect(name.getFirstName()).andReturn(firstName);
		final String middleName = "Middle";
		EasyMock.expect(name.getMiddleName()).andReturn(middleName);
		final String lastName = "Last";
		EasyMock.expect(name.getLastName()).andReturn(lastName);
		final String suffix = "Suffix";
		EasyMock.expect(name.getSuffix()).andReturn(suffix);
		getPendingVoterRegistrationDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new PendingVoterRegistrationDAO() {
			@Override
			public void makeAllPersistent(final Collection objects) {
				final List<PendingVoterRegistrationStatus> statuses = (List<PendingVoterRegistrationStatus>) objects;
				assertEquals("There is one status", 1, statuses.size());
				final PendingVoterRegistrationStatus status = statuses.get(0);
				assertEquals("The identifier is set", id, status.getId().longValue());
				assertNotNull("The name is set", status.getName());
				assertNotNull("The completion date is set", status.getCompletionDate());
				assertEquals("The downloaded by is set", user, status.getDownloadedBy());
			}
		});
		getPendingVoterRegistrationDAO().makeAllTransient(pendingVoterRegistrations);
		replayAll();

		getPendingVoterRegistrationService().makeComplete(user, pendingVoterRegistrations);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.PendingVoterRegistrationService#makeTimeout(java.util.List)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Nov 5, 2012
	 * @version Nov 15, 2012
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public final void testMakeTimeout() {
		final long id = 98723l;
		final PendingVoterName name = createMock("Name", PendingVoterName.class);
		final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
				PendingVoterRegistration.class);
		final List<PendingVoterRegistration> pendingVoterRegistrations = Arrays.asList(pendingVoterRegistration);
		EasyMock.expect(pendingVoterRegistration.getId()).andReturn(id);
		EasyMock.expect(pendingVoterRegistration.getName()).andReturn(name);
		EasyMock.expect(name.getEncrypted()).andReturn(null);
		final String title = "Title";
		EasyMock.expect(name.getTitle()).andReturn(title);
		final String firstName = "First";
		EasyMock.expect(name.getFirstName()).andReturn(firstName);
		final String middleName = "Middle";
		EasyMock.expect(name.getMiddleName()).andReturn(middleName);
		final String lastName = "Last";
		EasyMock.expect(name.getLastName()).andReturn(lastName);
		final String suffix = "Suffix";
		EasyMock.expect(name.getSuffix()).andReturn(suffix);
		getPendingVoterRegistrationDAO().makeAllPersistent((Collection) EasyMock.anyObject());
		EasyMock.expectLastCall().andDelegateTo(new PendingVoterRegistrationDAO() {
			@Override
			public void makeAllPersistent(final Collection objects) {
				final List<PendingVoterRegistrationStatus> statuses = (List<PendingVoterRegistrationStatus>) objects;
				assertEquals("There is one status", 1, statuses.size());
				final PendingVoterRegistrationStatus status = statuses.get(0);
				assertEquals("The identifier is set", id, status.getId().longValue());
				assertNotNull("The name is set", status.getName());
				assertNotNull("The completion date is set", status.getCompletionDate());
				assertNull("The downloaded by is not set", status.getDownloadedBy());
			}
		});
		getPendingVoterRegistrationDAO().makeAllTransient(pendingVoterRegistrations);
		replayAll();

		getPendingVoterRegistrationService().makeTimeout(pendingVoterRegistrations);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.PendingVoterRegistrationService#save(com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem setting up the encryption.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	@Test
	public final void testSave() throws Exception {
        final PendingVoterRegistration pendingVoterRegistration = createMock("PendingVoterRegistration",
           				PendingVoterRegistration.class);
        getPendingVoterRegistrationDAO().makePersistent(pendingVoterRegistration);
		replayAll();

		getPendingVoterRegistrationService().save(pendingVoterRegistration);

		verifyAll();
	}

	/**
	 * Creates a pending voter registration service.
	 * 
	 * @author IanBrown
	 * @return the pending voter registration service.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private PendingVoterRegistrationService createPendingVoterRegistrationService() {
		final PendingVoterRegistrationService pendingVoterRegistrationService = new PendingVoterRegistrationService();
		return pendingVoterRegistrationService;
	}

	/**
	 * Gets the encryption service.
	 * 
	 * @author IanBrown
	 * @return the encryption service.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private EncryptionService getEncryptionService() {
		return encryptionService;
	}

	/**
	 * Gets the pending voter registration DAO.
	 * 
	 * @author IanBrown
	 * @return the pending voter registration DAO.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private PendingVoterRegistrationDAO getPendingVoterRegistrationDAO() {
		return pendingVoterRegistrationDAO;
	}

	/**
	 * Gets the pending voter registration service.
	 * 
	 * @author IanBrown
	 * @return the pending voter registration service.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private PendingVoterRegistrationService getPendingVoterRegistrationService() {
		return pendingVoterRegistrationService;
	}

    public PendingVoterRegistrationCipherTest getPendingVoterRegistrationCipher() {
        return pendingVoterRegistrationCipher;
    }

    /**
	 * Sets the encryption service.
	 * 
	 * @author IanBrown
	 * @param encryptionService
	 *            the encryption service to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private void setEncryptionService(final EncryptionService encryptionService) {
		this.encryptionService = encryptionService;
	}

	/**
	 * Sets the pending voter registration DAO.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistrationDAO
	 *            the pending voter registration DAO to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private void setPendingVoterRegistrationDAO(final PendingVoterRegistrationDAO pendingVoterRegistrationDAO) {
		this.pendingVoterRegistrationDAO = pendingVoterRegistrationDAO;
	}

	/**
	 * Sets the pending voter registration service.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistrationService
	 *            the pending voter registration service to set.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	private void setPendingVoterRegistrationService(final PendingVoterRegistrationService pendingVoterRegistrationService) {
		this.pendingVoterRegistrationService = pendingVoterRegistrationService;
	}

}
