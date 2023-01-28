/**
 * 
 */
package com.bearcode.ovf.tools.vip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.vip.xml.ObjectFactory;
import com.bearcode.ovf.tools.vip.xml.VipObject;

/**
 * Test for {@link VipLoadJob}.
 * 
 * @author IanBrown
 * 
 * @since Jun 25, 2012
 * @version May 7, 2013
 */
public final class VipLoadJobTest extends EasyMockSupport {

	/**
	 * the valet.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipLoadJobValet valet;

	/**
	 * the VIP load job to test.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipLoadJob vipLoadJob;

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipService vipService;

	private OvfPropertyService propertyService;

	/**
	 * Sets up to test the VIP load job.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@Before
	public final void setUpVipLoadJob() {
		setValet(createMock("Valet", VipLoadJobValet.class));
		setVipLoadJob(new VipLoadJob());
		getVipLoadJob().setValet(getValet());
		setVipService(createMock("VipService", VipService.class));
		setPropertyService(createMock("OvfPropertyService", OvfPropertyService.class));
	}

	/**
	 * Tears down the VIP load job after testing.
	 * 
	 * @author IanBrown
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	@After
	public final void tearDownVipLoadJob() {
		setVipService(null);
		setVipLoadJob(null);
		setValet(null);
	}

	/**
	 * Test method for {@link org.springframework.scheduling.quartz.QuartzJobBean#execute(org.quartz.JobExecutionContext)}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem setting up the scheduler.
	 * @throws JAXBException
	 *             if there is a problem setting up the VIP object to read.
	 * @throws IOException
	 *             if there is a problem with the output stream.
	 * @since Jun 25, 2012
	 * @version Oct 11, 2012
	 */
	@Test
	public final void testExecute() throws SchedulerException, JAXBException, IOException {
		final String vipJobTarget = "Valid Job Target";
		final JobExecutionContext context = createJobExecutionContext(vipJobTarget, null);
		final ObjectFactory objectFactory = new ObjectFactory();
		final VipObject vipObject = objectFactory.createVipObject();
		final JAXBContext jaxbContext = JAXBContext.newInstance(VipObject.class.getPackage().getName());
		final Marshaller marshaller = jaxbContext.createMarshaller();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		marshaller.marshal(vipObject, baos);
		baos.close();
		final InputStream is = new ByteArrayInputStream(baos.toByteArray());
		EasyMock.expect(getValet().acquireInputStream(vipJobTarget)).andReturn(is);
		EasyMock.expect(getValet().loadVipObject(is)).andDelegateTo(new VipLoadJob.VipLoadJobValetImpl());
		final Date lastModified = new Date();
		EasyMock.expect(getValet().acquireLastModified(vipJobTarget)).andReturn(lastModified);
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		getVipService().convert((VipObject) EasyMock.anyObject(), EasyMock.eq(lastModified));
		replayAll();

		getVipLoadJob().execute(context);

		verifyAll();
	}

	/**
	 * Test method for {@link org.springframework.scheduling.quartz.QuartzJobBean#execute(org.quartz.JobExecutionContext)} for the
	 * case where the input stream is not valid.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem setting up the scheduler.
	 * @throws IOException
	 *             if there is a problem setting up the valet.
	 * @since Jun 25, 2012
	 * @version Aug 17, 2012
	 */
	@Test
	public final void testExecute_invalidJobTarget() throws SchedulerException, IOException {
		final String vipJobTarget = "Invalid Job Target";
		final JobExecutionContext context = createJobExecutionContext(vipJobTarget, null);
		final String garbage = "Garbage";
		final InputStream is = new ByteArrayInputStream(garbage.getBytes());
		EasyMock.expect(getValet().acquireInputStream(vipJobTarget)).andReturn(is);
		EasyMock.expect(getValet().loadVipObject(is)).andDelegateTo(new VipLoadJob.VipLoadJobValetImpl());
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getVipLoadJob().execute(context);

		verifyAll();
	}

	/**
	 * Test method for {@link org.springframework.scheduling.quartz.QuartzJobBean#execute(org.quartz.JobExecutionContext)} for the
	 * case where there is no job target.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem setting up the scheduler.
	 * @throws IOException
	 *             if there is a problem setting up the valet.
	 * @since Sep 26, 2012
	 * @version Sep 26, 2012
	 */
	@Test
	public final void testExecute_noJobTarget() throws SchedulerException, IOException {
		final JobExecutionContext context = createJobExecutionContext(null, null);
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getVipLoadJob().execute(context);

		verifyAll();
	}

	/**
	 * Test method for {@link org.springframework.scheduling.quartz.QuartzJobBean#execute(org.quartz.JobExecutionContext)} for the
	 * case where the job target doesn't exist.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem setting up the scheduler.
	 * @throws IOException
	 *             if there is a problem setting up the valet.
	 * @since Jun 25, 2012
	 * @version Aug 17, 2012
	 */
	@Test
	public final void testExecute_noSuchJobTarget() throws SchedulerException, IOException {
		final String vipJobTarget = "No Such Job Target";
		final JobExecutionContext context = createJobExecutionContext(vipJobTarget, null);
		EasyMock.expect(getValet().acquireInputStream(vipJobTarget)).andReturn(null);
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getVipLoadJob().execute(context);

		verifyAll();
	}

	/**
	 * Test method for {@link org.springframework.scheduling.quartz.QuartzJobBean#execute(org.quartz.JobExecutionContext)} for the
	 * case where there is a VIP job configuration provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SchedulerException
	 *             if there is a problem setting up the scheduler.
	 * @throws JAXBException
	 *             if there is a problem setting up the VIP object to read.
	 * @throws IOException
	 *             if there is a problem with the output stream.
	 * @since Sep 26, 2012
	 * @version Oct 11, 2012
	 */
	@Test
	public final void testExecute_vipJobConfiguration() throws SchedulerException, JAXBException, IOException {
		final VipJobConfiguration vipJobConfiguration = createMock("VipJobConfiguration", VipJobConfiguration.class);
		final String configurationTarget = "Valid Job Target";
		EasyMock.expect(vipJobConfiguration.getVipJobTarget()).andReturn(configurationTarget);
		final JobExecutionContext context = createJobExecutionContext(null, vipJobConfiguration);
		final ObjectFactory objectFactory = new ObjectFactory();
		final VipObject vipObject = objectFactory.createVipObject();
		final JAXBContext jaxbContext = JAXBContext.newInstance(VipObject.class.getPackage().getName());
		final Marshaller marshaller = jaxbContext.createMarshaller();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		marshaller.marshal(vipObject, baos);
		baos.close();
		final InputStream is = new ByteArrayInputStream(baos.toByteArray());
		EasyMock.expect(getValet().acquireInputStream(configurationTarget)).andReturn(is);
		EasyMock.expect(getValet().loadVipObject(is)).andDelegateTo(new VipLoadJob.VipLoadJobValetImpl());
		final Date lastModified = new Date();
		EasyMock.expect(getValet().acquireLastModified(configurationTarget)).andReturn(lastModified);
		getVipService().convert((VipObject) EasyMock.anyObject(), EasyMock.eq(lastModified));
		EasyMock.expect(getPropertyService().getProperty(EasyMock.anyObject(OvfPropertyNames.class))).andReturn("true");
		replayAll();

		getVipLoadJob().execute(context);

		verifyAll();
	}

	/**
	 * Creates the job execution context.
	 * 
	 * @author IanBrown
	 * @param vipJobTarget
	 *            the target for the VIP job.
	 * @param vipJobConfiguration
	 *            the VIP job configuration.
	 * @return the job execution context.
	 * @throws SchedulerException
	 *             if there is a problem setting up the job execution context.
	 * @since Jun 26, 2012
	 * @version May 7, 2013
	 */
	private JobExecutionContext createJobExecutionContext(final String vipJobTarget, final VipJobConfiguration vipJobConfiguration)
			throws SchedulerException {
		final JobExecutionContext context = createMock("JobExecutionContext", JobExecutionContext.class);
		final Scheduler scheduler = createMock("Scheduler", Scheduler.class);
		EasyMock.expect(context.getScheduler()).andReturn(scheduler).anyTimes();
		JobDetail jobDetail = createMock("JobDetail", JobDetail.class);
		EasyMock.expect(context.getJobDetail()).andReturn(jobDetail).anyTimes();
		EasyMock.expect(jobDetail.getName()).andReturn("Test Job").anyTimes();
		final SchedulerContext schedulerContext = new SchedulerContext();
		EasyMock.expect(scheduler.getContext()).andReturn(schedulerContext).anyTimes();
		final JobDataMap mergedJobDataMap = new JobDataMap();
		EasyMock.expect(context.getMergedJobDataMap()).andReturn(mergedJobDataMap).anyTimes();
		if (vipJobTarget != null) {
			mergedJobDataMap.put("vipJobTarget", vipJobTarget);
		}
		if (vipJobConfiguration != null) {
			mergedJobDataMap.put("vipJobConfiguration", vipJobConfiguration);
		}
		mergedJobDataMap.put("vipService", getVipService());
		mergedJobDataMap.put("propertyService", getPropertyService());
		return context;
	}

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipLoadJobValet getValet() {
		return valet;
	}

	/**
	 * Gets the VIP load job.
	 * 
	 * @author IanBrown
	 * @return the VIP load job.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private VipLoadJob getVipLoadJob() {
		return vipLoadJob;
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

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *            the valet to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private void setValet(final VipLoadJobValet valet) {
		this.valet = valet;
	}

	/**
	 * Sets the VIP load job.
	 * 
	 * @author IanBrown
	 * @param vipLoadJob
	 *            the VIP load job to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private void setVipLoadJob(final VipLoadJob vipLoadJob) {
		this.vipLoadJob = vipLoadJob;
	}

	/**
	 * Sets the vipService.
	 * 
	 * @author IanBrown
	 * @param vipService
	 *            the vipService to set.
	 * @since Jun 25, 2012
	 * @version Jun 25, 2012
	 */
	private void setVipService(final VipService vipService) {
		this.vipService = vipService;
	}

	public OvfPropertyService getPropertyService() {
		return propertyService;
	}

	public void setPropertyService(OvfPropertyService propertyService) {
		this.propertyService = propertyService;
	}
}
