/**
 * 
 */
package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.Officer;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.service.email.EmailTemplates;
import org.apache.commons.mail.EmailException;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link SendEmailToOfficer}.
 * 
 * @author IanBrown
 * 
 * @since Jul 26, 2012
 * @version Jul 26, 2012
 */
public final class SendEmailToOfficerTest extends EasyMockSupport {

	/**
	 * recipient index: to all election official.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private static final int TO_ALL_ELECTION_OFFICIAL = -1;

	/**
	 * recipient index: to all election official.
	 *
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private static final int TO_FIRST_ELECTION_OFFICIAL = 0;

	/**
	 * recipient type: absentee clerk.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private static final int TO_ABSENTEE_CLERK = 1;

	/**
	 * the send email to officer to test.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private SendEmailToOfficer sendEmailToOfficer;

	/**
	 * the faces service to use.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private FacesService facesService;

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * the email service.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private EmailService emailService;

	/**
	 * Sets up the send Email to officer to test.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Before
	public final void setUpSendEmailToOfficer() {
		setFacesService(createMock("FacesService", FacesService.class));
		setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
		setEmailService(createMock("EmailService", EmailService.class));
		setSendEmailToOfficer(createSendEmailToOfficer());
		getSendEmailToOfficer().setFacesService(getFacesService());
		getSendEmailToOfficer().setLocalOfficialService(getLocalOfficialService());
		getSendEmailToOfficer().setEmailService(getEmailService());
	}

	/**
	 * Tears down the send Email to officer after testing.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@After
	public final void tearDownSendEmailToOfficer() {
		setSendEmailToOfficer(null);
		setEmailService( null );
		setLocalOfficialService( null );
		setFacesService( null );
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.SendEmailToOfficer#sendAllLeoEmail(java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest)}
	 * for an election official.
	 * 
	 * @author IanBrown
	 * 
	 * @throws EmailException
	 *             if there is a problem setting up to send the email.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testSendAllLeoEmail_toElectionOfficial() throws EmailException {
		final Long stateId = 20122607l;
		final Integer recipientType = TO_ALL_ELECTION_OFFICIAL;
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfig(request.getServerName() + request.getContextPath())).andReturn(faceConfig);
		final String relativePrefix = "relativePrefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(relativePrefix);
		final String approvedFileName = "ApprovedFile.name";
		EasyMock.expect(getFacesService().getApprovedFileName(EmailTemplates.XML_OFFICER_SNAPSHOT, relativePrefix)).andReturn(
				approvedFileName);
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final Collection<LocalOfficial> leos = Arrays.asList(localOfficial);
		EasyMock.expect(getLocalOfficialService().findForState(stateId)).andReturn(leos);
		final Officer officer = new Officer();
        officer.setFirstName("leo");
        officer.setOfficeName("Local Election Official");
        officer.setEmail("leo@email");
        final List<Officer> officers = Arrays.asList(officer);
        EasyMock.expect(localOfficial.getOfficers()).andReturn(officers).anyTimes();
		getEmailService().queue((Email) EasyMock.anyObject());
		replayAll();

		final String actualResponse = getSendEmailToOfficer().sendAllLeoEmail(stateId, recipientType, request);

		assertEquals("The response indicates that everything was sent", leos.size() + " emails sent successfully", actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.SendEmailToOfficer#sendAllLeoEmail(java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest)}
	 * for an election official.
	 *
	 * @author IanBrown
	 *
	 * @throws EmailException
	 *             if there is a problem setting up to send the email.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testSendAllLeoEmail_toElectionOfficial_severalTimes() throws EmailException {
		final Long stateId = 20122607l;
		final Integer recipientType = TO_ALL_ELECTION_OFFICIAL;
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfig( request.getServerName() + request.getContextPath() )).andReturn( faceConfig );
		final String relativePrefix = "relativePrefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(relativePrefix);
		final String approvedFileName = "ApprovedFile.name";
		EasyMock.expect(getFacesService().getApprovedFileName( EmailTemplates.XML_OFFICER_SNAPSHOT, relativePrefix )).andReturn(
				approvedFileName);
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final Collection<LocalOfficial> leos = Arrays.asList(localOfficial);
		EasyMock.expect(getLocalOfficialService().findForState(stateId)).andReturn(leos);
		final Officer officer = new Officer();
		officer.setFirstName("leo");
		officer.setOfficeName("Local Election Official");
		officer.setEmail( "leo@email" );
		final Officer officer2 = new Officer();
		officer2.setFirstName( "leo2" );
		officer2.setOfficeName( "Second Election Official" );
		officer2.setEmail( "leo_second@email" );
		final Officer officer3 = new Officer();
		officer3.setFirstName("leo3");
		officer3.setOfficeName("Third Election Official");
		officer3.setEmail("leo_third@email");
		final List<Officer> officers = Arrays.asList( new Officer[] {officer, officer2, officer3});
		EasyMock.expect(localOfficial.getOfficers()).andReturn(officers).anyTimes();
		getEmailService().queue( (Email) EasyMock.anyObject() );
		EasyMock.expectLastCall().times( 3 );
		replayAll();

		final String actualResponse = getSendEmailToOfficer().sendAllLeoEmail(stateId, recipientType, request);

		assertEquals("The response indicates that everything was sent", leos.size() + " emails sent successfully", actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.SendEmailToOfficer#sendAllLeoEmail(java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest)}
	 * for an election official.
	 *
	 * @author IanBrown
	 *
	 * @throws EmailException
	 *             if there is a problem setting up to send the email.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testSendAllLeoEmail_toElectionOfficial_noDuplicates() throws EmailException {
		final Long stateId = 20122607l;
		final Integer recipientType = TO_ALL_ELECTION_OFFICIAL;
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfig( request.getServerName() + request.getContextPath() )).andReturn( faceConfig );
		final String relativePrefix = "relativePrefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(relativePrefix);
		final String approvedFileName = "ApprovedFile.name";
		EasyMock.expect(getFacesService().getApprovedFileName( EmailTemplates.XML_OFFICER_SNAPSHOT, relativePrefix )).andReturn(
				approvedFileName);
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final Collection<LocalOfficial> leos = Arrays.asList(localOfficial);
		EasyMock.expect(getLocalOfficialService().findForState(stateId)).andReturn( leos );
		final Officer officer = new Officer();
		officer.setFirstName( "leo" );
		officer.setOfficeName( "Local Election Official" );
		officer.setEmail( "leo@email" );
		final Officer officer2 = new Officer();
		officer2.setFirstName( "leo2" );
		officer2.setOfficeName( "Second Election Official" );
		officer2.setEmail( "leo@email" ); //same email
		final Officer officer3 = new Officer();
		officer3.setFirstName( "leo3" );
		officer3.setOfficeName( "Third Election Official" );
		officer3.setEmail( "leo@email" ); //same email
		final List<Officer> officers = Arrays.asList( new Officer[] {officer, officer2, officer3});
		EasyMock.expect(localOfficial.getOfficers()).andReturn( officers ).anyTimes();
		getEmailService().queue( (Email) EasyMock.anyObject() ); //should call queue() only once
		replayAll();

		final String actualResponse = getSendEmailToOfficer().sendAllLeoEmail( stateId, recipientType, request );

		assertEquals("The response indicates that everything was sent", leos.size() + " emails sent successfully", actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.SendEmailToOfficer#sendAllLeoEmail(java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest)}
	 * for an election official.
	 *
	 * @author IanBrown
	 *
	 * @throws EmailException
	 *             if there is a problem setting up to send the email.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testSendAllLeoEmail_toSpecificElectionOfficial() throws EmailException {
		final Long stateId = 20122607l;
		final Integer recipientType = TO_FIRST_ELECTION_OFFICIAL;
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfig( request.getServerName() + request.getContextPath() )).andReturn(faceConfig);
		final String relativePrefix = "relativePrefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(relativePrefix);
		final String approvedFileName = "ApprovedFile.name";
		EasyMock.expect(getFacesService().getApprovedFileName( EmailTemplates.XML_OFFICER_SNAPSHOT, relativePrefix )).andReturn(
				approvedFileName);
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final Collection<LocalOfficial> leos = Arrays.asList(localOfficial);
		EasyMock.expect(getLocalOfficialService().findForState(stateId)).andReturn( leos );
		final Officer officer = new Officer();
		officer.setFirstName( "leo" );
		officer.setOfficeName( "Local Election Official" );
		officer.setEmail( "leo@email" );
		final Officer officer2 = new Officer();
		officer2.setFirstName( "leo2" );
		officer2.setOfficeName( "Second Election Official" );
		officer2.setEmail( "leo@email" ); //same email
		final Officer officer3 = new Officer();
		officer3.setFirstName( "leo3" );
		officer3.setOfficeName( "Third Election Official" );
		officer3.setEmail( "leo@email" ); //same email
		final List<Officer> officers = Arrays.asList( new Officer[] {officer, officer2, officer3});
		EasyMock.expect(localOfficial.getOfficers()).andReturn( officers ).anyTimes();
		getEmailService().queue( (Email) EasyMock.anyObject() ); //should call queue() only once
		replayAll();

		final String actualResponse = getSendEmailToOfficer().sendAllLeoEmail( stateId, recipientType, request );

		assertEquals("The response indicates that everything was sent", leos.size() + " emails sent successfully", actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.SendEmailToOfficer#sendOneLeoEmail(java.lang.Long, java.lang.Integer, javax.servlet.http.HttpServletRequest)}
	 * to an absentee clerk.
	 * 
	 * @author IanBrown
	 * 
	 * @throws EmailException
	 *             if there is a problem setting up to send the email.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testSendOneLeoEmail_toAbsenteeClerk() throws EmailException {
		final Long leoId = 260712l;
		final Integer recipientType = TO_ABSENTEE_CLERK;
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		EasyMock.expect(getLocalOfficialService().findById(leoId)).andReturn(localOfficial);
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(getFacesService().findConfig(request.getServerName() + request.getContextPath())).andReturn(faceConfig);
		final String relativePrefix = "relativePrefix";
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(relativePrefix);
		final String approvedFileName = "ApprovedFile.name";
		EasyMock.expect(getFacesService().getApprovedFileName(EmailTemplates.XML_OFFICER_SNAPSHOT, relativePrefix)).andReturn(
				approvedFileName);
        final Officer officer1 = new Officer();
        officer1.setFirstName("leo");
        officer1.setOfficeName("Local Election Official");
        officer1.setEmail("leo@email");
        final Officer officer2 = new Officer();
        officer2.setFirstName("lovc");
        officer2.setOfficeName("Absentee voter clerk");
        officer2.setEmail("lovc@email");
        final List<Officer> officers = Arrays.asList(officer1,officer2);
        EasyMock.expect(localOfficial.getOfficers()).andReturn(officers).anyTimes();
		getEmailService().queue((Email) EasyMock.anyObject());
		replayAll();

		final String actualResponse = getSendEmailToOfficer().sendOneLeoEmail(leoId, recipientType, request);

		assertEquals("The email was sent", "Email was sent", actualResponse);
		verifyAll();
	}

	/**
	 * Creates a send Email to officer.
	 * 
	 * @author IanBrown
	 * @return the send Email to officer.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private SendEmailToOfficer createSendEmailToOfficer() {
		return new SendEmailToOfficer();
	}

	/**
	 * Gets the email service.
	 * 
	 * @author IanBrown
	 * @return the email service.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private EmailService getEmailService() {
		return emailService;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Gets the send Email to officer.
	 * 
	 * @author IanBrown
	 * @return the send Email to officer.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private SendEmailToOfficer getSendEmailToOfficer() {
		return sendEmailToOfficer;
	}

	/**
	 * Sets the email service.
	 * 
	 * @author IanBrown
	 * @param emailService
	 *            the email service to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setEmailService(final EmailService emailService) {
		this.emailService = emailService;
	}

	/**
	 * Sets the faces service.
	 * 
	 * @author IanBrown
	 * @param facesService
	 *            the faces service to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

	/**
	 * Sets the send Email to officer.
	 * 
	 * @author IanBrown
	 * @param sendEmailToOfficer
	 *            the send Email to officer to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setSendEmailToOfficer(final SendEmailToOfficer sendEmailToOfficer) {
		this.sendEmailToOfficer = sendEmailToOfficer;
	}
}
