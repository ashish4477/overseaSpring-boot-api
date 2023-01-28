/**
 * 
 */
package com.bearcode.ovf.tools.pendingregistration;

import java.util.List;

import javax.xml.datatype.Duration;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.service.PendingVoterRegistrationService;

/**
 * Extended {@link QuartzJobBean} implementation of {@link StatefulJob} to handle cleanup of pending voter registrations.
 * 
 * @author IanBrown
 * 
 * @since Nov 26, 2012
 * @version Nov 26, 2012
 */
public class PendingVoterRegistrationJob extends QuartzJobBean implements StatefulJob {

	/**
	 * the logger for the class.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(PendingVoterRegistrationJob.class);

	/**
	 * the pending voter registration service.
	 * 
	 * @author IanBrown
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private PendingVoterRegistrationService pendingVoterRegistrationService;

	private OvfPropertyService propertyService;

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

	public OvfPropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(OvfPropertyService propertyService) {
		this.propertyService = propertyService;
	}

	/** {@inheritDoc} */
	@Override
	protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
		if (!Boolean.parseBoolean(propertyService.getProperty(OvfPropertyNames.IS_JOB_ENABLED))) {
			return;
		}
		LOGGER.info("Cleaning up pending voter registrations");
		final List<PendingVoterRegistrationConfiguration> pendingVoterRegistrationConfigurations = getPendingVoterRegistrationService()
				.getPendingVoterRegistrationConfigurations();

		if (pendingVoterRegistrationConfigurations != null) {
			try {
				for (final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration : pendingVoterRegistrationConfigurations) {
					timeoutPendingVoterRegistrations(pendingVoterRegistrationConfiguration);
				}
			} catch (final Exception e) {
				LOGGER.error("Failed to timeout pending voter registrations", e);
				throw new JobExecutionException("Failed to timeout pending voter registrations", e, false);
			}
		}

		LOGGER.info("Finished cleaning up pending voter registrations");
	}

	/**
	 * Times out the pending voter registrations for the specified configuration.
	 * 
	 * @author IanBrown
	 * @param pendingVoterRegistrationConfiguration
	 *            the pending voter registration configuration.
	 * @throws Exception
	 *             if there is a problem timing out the voter registrations.
	 * @since Nov 26, 2012
	 * @version Nov 26, 2012
	 */
	private void timeoutPendingVoterRegistrations(final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration)
			throws Exception {
		final String votingState = pendingVoterRegistrationConfiguration.getVotingState();
		final String votingRegion = pendingVoterRegistrationConfiguration.getVotingRegion();
		final Duration timeout = pendingVoterRegistrationConfiguration.getTimeout();
		LOGGER.info("Timing out pending voter registrations for " + votingState + (votingRegion == null ? "" : " (" + votingRegion + ")") + " older than " + timeout);
		final List<PendingVoterRegistration> pendingVoterRegistrations = getPendingVoterRegistrationService()
				.findForConfiguration( pendingVoterRegistrationConfiguration, timeout, true );
		if (pendingVoterRegistrations != null && !pendingVoterRegistrations.isEmpty()) {
			getPendingVoterRegistrationService().makeTimeout(pendingVoterRegistrations);
			LOGGER.info("Timed out " + pendingVoterRegistrations.size() + " pending voter registrations");
		}
	}
}
