/**
 * 
 */
package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.PendingVoterRegistrationDAO;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.pendingregistration.PendingVoterName;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistrationStatus;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.Duration;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Service to provide access to {@link PendingVoterRegistration} objects.
 * 
 * @author IanBrown
 * 
 * @since Nov 5, 2012
 * @version Dec 13, 2012
 */
@Service
public class PendingVoterRegistrationService {
    private static Logger logger = LoggerFactory.getLogger(PendingVoterRegistrationService.class);

    //@Autowired
    //private PendingVoterRegistrationCipher pendingVoterRegistrationCipher;

	/**
	 * the service to encrypt/decrypt data.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	//@Autowired
	//private EncryptionService encryptionService;

	/**
	 * the configuration objects for the pending voter registration.
	 * 
	 * @author IanBrown
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	@Autowired(required = false)
	private List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations;

	/**
	 * the DAO used to save/retrieve the pending voter registrations.
	 * 
	 * @author IanBrown
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	@Autowired
	private PendingVoterRegistrationDAO pendingVoterRegistrationDAO;


	/**
	 * Deletes the pending voter registration.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistration
	 *            the pending voter registration.
	 * @since Nov 14, 2012
	 * @version Nov 15, 2012
	 */
	public void delete(final PendingVoterRegistration pendingVoterRegistration) {
		getPendingVoterRegistrationDAO().makeTransient(pendingVoterRegistration);
	}


	/**
	 * Finds the pending voter registration for the specified identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @return the pending voter registration.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	public PendingVoterRegistration findById(final Long id) {
		return getPendingVoterRegistrationDAO().findById(id);
	}

    public List<PendingVoterRegistration> findForConfiguration(final PendingVoterRegistrationConfiguration configuration)
            throws Exception {
        return findForConfiguration( configuration, false );
    }

        /**
         * Finds the pending voter registrations from the database for the specified state and optional region. This method ensures that
         * the personally identifiable information for the pending voter registration has been decrypted before it is returned.
         *
         * @param configuration
         *            the pending voter registration configuration.
         * @return the pending voter registrations.
         * @throws Exception
         *             if there is a problem with the decryption.
         * @since Nov 5, 2012
         * @version Nov 8, 2012
         */
    public List<PendingVoterRegistration> findForConfiguration(final PendingVoterRegistrationConfiguration configuration,
                                                               final boolean all ) throws Exception {
        final List<PendingVoterRegistration> pendingVoterRegistrations = getPendingVoterRegistrationDAO()
                .findForConfiguration( configuration, null, all );
		return pendingVoterRegistrations;
	}

    public List<PendingVoterRegistration> findForConfiguration(final PendingVoterRegistrationConfiguration configuration,
                                                               final Duration timeout ) throws Exception {
        return findForConfiguration( configuration, timeout, false );
    }

	/**
	 * Find the pending voter registrations for the state and region that are older than the timeout.
	 * 
     * @param configuration
     *            the pending voter registration configuration.
	 * @param timeout
	 *            the timeout duration.
	 * @return the pending voter registrations.
	 * @throws Exception
     *             if there is a problem with the configuration.
	 */
    public List<PendingVoterRegistration> findForConfiguration(final PendingVoterRegistrationConfiguration configuration,
                                                               final Duration timeout,
                                                               final boolean all) throws Exception {
        final List<PendingVoterRegistration> pendingVoterRegistrations = getPendingVoterRegistrationDAO()
                .findForConfiguration( configuration, timeout, all );
		return pendingVoterRegistrations;
	}

	/**
	 * Finds the pending voter registration configuration for the face, voting state, and voting region.
	 * 
	 * @author IanBrown
	 * @param administrator
	 *            if the user is an administrator.
	 * @param download
	 *            <code>true</code> if the pending data is to be downloaded, <code>false</code> if it is just being added.
	 * @param faceConfig
	 *            the face configuration.
	 * @param userFaceConfig
	 *            the user face configuration.
	 * @param votingState
	 *            the voting state.
	 * @param votingRegion
	 *            the voting region.
	 * @return the pending voter configuration or <code>null</code>.
	 * @since Nov 13, 2012
	 * @version Nov 28, 2012
	 */
	public PendingVoterRegistrationConfiguration findPendingVoterRegistrationConfiguration(final boolean administrator,
			final boolean download, final FaceConfig faceConfig, final FaceConfig userFaceConfig, final String votingState,
			final String votingRegion) {
		if (!administrator && download
				&& (userFaceConfig == null || !faceConfig.getRelativePrefix().equals(userFaceConfig.getRelativePrefix()))) {
			return null;
		}

		if (getPendingVoterRegistrationConfigurations() != null) {
			final String facePrefix = faceConfig == null ? null : faceConfig.getRelativePrefix();
			for (final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration : getPendingVoterRegistrationConfigurations()) {
				if (!pendingVoterRegistrationConfiguration.isEnabled()
						|| (pendingVoterRegistrationConfiguration.isRequireShs() && !facePrefix
								.equals(pendingVoterRegistrationConfiguration.getFacePrefix()))) {
					continue;
				}

				final String expectedVotingState = pendingVoterRegistrationConfiguration.getVotingState();
				if (expectedVotingState != null && !expectedVotingState.equals(votingState)) {
					continue;
				}

				/*final String expectedVotingRegion = pendingVoterRegistrationConfiguration.getVotingRegion();
				if (expectedVotingRegion != null && !expectedVotingRegion.equals(votingRegion)) {
					continue;
				}
				no Ability to setup configuration for voting region
				*/

				return pendingVoterRegistrationConfiguration;
			}
		}

		return null;
	}

	/**
	 * Finds the pending voter registration status for the specified identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @return the pending voter registration status.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	public PendingVoterRegistrationStatus findStatusById(final Long id) {
		return getPendingVoterRegistrationDAO().findStatusById(id);
	}

	/**
	 * Gets the encryption service.
	 * 
	 * @author IanBrown
	 * @return the encryption service.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	/*public EncryptionService getEncryptionService() {
		return encryptionService;
	}*/

	/**
	 * Gets the pending voter registration configurations.
	 * 
	 * @author IanBrown
	 * @return the pending voter registration configurations.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public List<PendingVoterRegistrationConfiguration> getPendingVoterRegistrationConfigurations() {
		return pendingVoterRegistrationConfigurations;
	}

	/**
	 * Gets the pending voter registration DAO.
	 * 
	 * @author IanBrown
	 * @return the pending voter registration DAO.
	 * @since Nov 5, 2012
	 * @version Nov 5, 2012
	 */
	public PendingVoterRegistrationDAO getPendingVoterRegistrationDAO() {
		return pendingVoterRegistrationDAO;
	}

	/**
	 * Marks the specified pending voter registrations as complete. The pending voter registrations themselves have all personally
	 * identifiable information and all answers removed.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user who downloaded the pending voter registrations.
	 * @param pendingVoterRegistrations
	 *            the pending voter registrations.
	 * @since Nov 5, 2012
	 * @version Nov 15, 2012
	 */
	public void makeComplete(final OverseasUser user, final List<PendingVoterRegistration> pendingVoterRegistrations) {
		createStatuses(user, pendingVoterRegistrations);
	}

	/**
	 * Times out the specified pending voter registations.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistrations
	 *            the pending voter registrations.
	 * @since Nov 5, 2012
	 * @version Nov 15, 2012
	 */
	public void makeTimeout(final List<PendingVoterRegistration> pendingVoterRegistrations) {
		createStatuses(null, pendingVoterRegistrations);
	}

	/**
	 * Saves the pending voter registration to the database. This method ensures that the personally identifiable information for
	 * the pending voter registration is encrypted before it is saved.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistration
	 *            the pending voter registration.
	 * @throws Exception
	 *             if there is a problem saving the pending voter registration.
	 * @since Nov 5, 2012
	 * @version Nov 7, 2012
	 */
	public void save(final PendingVoterRegistration pendingVoterRegistration) throws Exception {
		getPendingVoterRegistrationDAO().makePersistent(pendingVoterRegistration);
	}


	/**
	 * Sets the pending voter registration configurations.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistrationConfigurations
	 *            the pending voter registration configurations to set.
	 * @since Nov 13, 2012
	 * @version Nov 13, 2012
	 */
	public void setPendingVoterRegistrationConfigurations(
			final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations) {
		this.pendingVoterRegistrationConfigurations = pendingVoterRegistrationConfigurations;
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
	public void setPendingVoterRegistrationDAO(final PendingVoterRegistrationDAO pendingVoterRegistrationDAO) {
		this.pendingVoterRegistrationDAO = pendingVoterRegistrationDAO;
	}

	/**
	 * Creates the statuses for the specified pending voter registrations.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the user - may by <code>null</code>.
	 * @param pendingVoterRegistrations
	 *            the pending voter registrations.
	 * @since Nov 5, 2012
	 * @version Nov 15, 2012
	 */
	private void createStatuses(final OverseasUser user, final List<PendingVoterRegistration> pendingVoterRegistrations) {
		final List<PendingVoterRegistrationStatus> statuses = new LinkedList<PendingVoterRegistrationStatus>();
		final Date completionDate = new Date();
		for (final PendingVoterRegistration pendingVoterRegistration : pendingVoterRegistrations) {
			final PendingVoterRegistrationStatus status = new PendingVoterRegistrationStatus();
			status.setId(pendingVoterRegistration.getId());
			status.setName(new PendingVoterName(pendingVoterRegistration.getName()));
			status.setCompletionDate(completionDate);
			status.setDownloadedBy(user);
			statuses.add(status);
		}
		getPendingVoterRegistrationDAO().makeAllPersistent(statuses);
		getPendingVoterRegistrationDAO().makeAllTransient(pendingVoterRegistrations);
	}

	/**
	 * Finds the pending voter registration configuration for the input wizard context.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param wizardResults
	 *            the wizard results from the wizard context.
	 * @return the corresponding pending voter registration configuration or <code>null</code>.
	 * @since Nov 13, 2012
	 * @version Nov 28, 2012
	 */
    public PendingVoterRegistrationConfiguration findPendingVoterRegistrationConfiguration(final WizardContext wizardContext,
                                                                                           final WizardResults wizardResults) {
		//final VotingRegion region = wizardResults.getVotingRegion();
		final String votingState = wizardResults.getVotingAddress().getState();
		final String votingRegion = wizardResults.getEodRegionId(); //region.getName();
		final FaceConfig faceConfig = wizardContext.getCurrentFace();
		return findPendingVoterRegistrationConfiguration(false, false, faceConfig, faceConfig, votingState, votingRegion);
	}

}
