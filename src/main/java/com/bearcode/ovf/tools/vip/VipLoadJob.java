/**
 * 
 */
package com.bearcode.ovf.tools.vip;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.vip.xml.VipObject;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Date;

/**
 * Quartz job to load VIP data.
 * 
 * @author IanBrown
 * 
 * @since Jun 25, 2012
 * @version May 7, 2013
 */
public class VipLoadJob extends QuartzJobBean implements StatefulJob {

	private OvfPropertyService propertyService;

	public OvfPropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(OvfPropertyService propertyService) {
		this.propertyService = propertyService;
	}

	/**
	 * Internal implementation of {@link VipLoadJobValet}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 25, 2012
	 * @version Oct 11, 2012
	 */
	final static class VipLoadJobValetImpl implements VipLoadJobValet {

		/** {@inheritDoc} */
		@Override
		public final InputStream acquireInputStream(final String vipJobTarget) throws IOException {
			if (vipJobTarget != null) {
                try {
                    return new FileInputStream(vipJobTarget);
                } catch (FileNotFoundException e) {
                    //file not found, it's not a big deal
                    LOGGER.warn( "VipLoadJob: file not found - " + vipJobTarget );
                    return null;
                }
            }

            // no name, no stream, no exception
            return null;
		}

		/** {@inheritDoc} */
		@Override
		public final Date acquireLastModified(final String vipJobTarget) {
			final File file = new File(vipJobTarget);
			final Date lastModified = file.exists() ? new Date(file.lastModified()) : null;
			return lastModified;
		}

		/** {@inheritDoc} */
		@Override
		public final VipObject loadVipObject(final InputStream is) {
			try {
				final JAXBContext context = JAXBContext.newInstance(VipObject.class.getPackage().getName());
				final Unmarshaller unmarshaller = context.createUnmarshaller();
				final VipObject vipObject = (VipObject) unmarshaller.unmarshal(is);
				return vipObject;
			} catch (final JAXBException e) {
				LOGGER.warn("Failed to load VIP object", e);
				return null;
			}
		}
	}

	/**
	 * the logger for the class.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private final static Logger LOGGER = LoggerFactory.getLogger(VipLoadJob.class);

	/**
	 * the valet used to acquire the data.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipLoadJobValet valet = new VipLoadJobValetImpl();

	/**
	 * the target of the VIP job.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private String vipJobTarget;

	/**
	 * the optional Spring controlled job configuration.
	 * 
	 * @author IanBrown
	 * @since Sep 26, 2012
	 * @version Sep 26, 2012
	 */
	private VipJobConfiguration vipJobConfiguration;

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipService vipService;

	/**
	 * is the loader already running?
	 * 
	 * @author IanBrown
	 * @since Aug 21, 2012
	 * @version Aug 21, 2012
	 */
	private static boolean running = false;

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
	 * Gets the VIP job configuration.
	 * 
	 * @author IanBrown
	 * @return the VIP job configuration.
	 * @since Sep 26, 2012
	 * @version Sep 26, 2012
	 */
	public VipJobConfiguration getVipJobConfiguration() {
		return vipJobConfiguration;
	}

	/**
	 * Gets the VIP job target.
	 * 
	 * @author IanBrown
	 * @return the VIP job target.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public String getVipJobTarget() {
		return vipJobTarget;
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public VipService getVipService() {
		return vipService;
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
	 * Sets the VIP job configuration.
	 * 
	 * @author IanBrown
	 * @param vipJobConfiguration
	 *            the VIP job configuration to set.
	 * @since Sep 26, 2012
	 * @version Sep 26, 2012
	 */
	public void setVipJobConfiguration(final VipJobConfiguration vipJobConfiguration) {
		this.vipJobConfiguration = vipJobConfiguration;
	}

	/**
	 * Sets the VIP job target.
	 * 
	 * @author IanBrown
	 * @param vipJobTarget
	 *            the VIP job target to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void setVipJobTarget(final String vipJobTarget) {
		this.vipJobTarget = vipJobTarget;
	}

	/**
	 * Sets the VIP service.
	 * 
	 * @author IanBrown
	 * @param vipService
	 *            the VIP service to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	public void setVipService(final VipService vipService) {
		this.vipService = vipService;
	}

	/** {@inheritDoc} */
	@Override
	protected void executeInternal(final JobExecutionContext context) throws JobExecutionException {
		if (!Boolean.parseBoolean(getPropertyService().getProperty(OvfPropertyNames.IS_JOB_ENABLED))) {
			return;
		}
		synchronized (VipLoadJob.class) {
			if (running) {
				return;
			}
			running = true;
		}

		String jobName = context.getJobDetail().getName();
		String jobTarget = null;
		try {
			jobTarget = getVipJobTarget();
			if (jobTarget == null || jobTarget.isEmpty()) {
				if (getVipJobConfiguration() == null) {
					LOGGER.info(jobName + ": No VIP job configuration provided");
					return;
				}
				jobTarget = getVipJobConfiguration().getVipJobTarget();
				if (jobTarget == null) {
					LOGGER.info(jobName + ": No VIP job target provided");
					return;
				}
			}

			final InputStream is = getValet().acquireInputStream(jobTarget);

			if (is == null) {
				LOGGER.info(jobName + ": No VIP data available at " + jobTarget);
				return;
			}

			LOGGER.info(jobName + ": Loading VIP data from " + jobTarget);
			final VipObject vipObject = getValet().loadVipObject(is);

			if (vipObject != null) {
				final Date lastModified = getValet().acquireLastModified(jobTarget);
				getVipService().convert(vipObject, lastModified);
			}

			LOGGER.info(jobName + ": Loaded VIP data from " + jobTarget);

		} catch (final Exception e) {
			LOGGER.error(jobName + ": Failed to load VIP data from " + jobTarget, e);
			throw new JobExecutionException("Failed to load VIP data", e, false);
		} finally {
			synchronized (VipLoadJob.class) {
				running = false;
			}
		}
	}
}
