/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Date;

import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.service.*;
import org.apache.commons.mail.EmailException;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import com.bearcode.ovf.actions.commons.AbstractControllerCheck;
import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.PdfFormTrack;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.email.Email;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.PdfGeneratorException;
import com.bearcode.ovf.tools.pdf.generator.crypto.CipherService;

/**
 * Extended {@link AbstractControllerCheck} test for {@link CreateForm}.
 * 
 * @author IanBrown
 * 
 * @since Jun 18, 2012
 * @version Nov 14, 2012
 */
public final class CreateFormTest extends AbstractControllerCheck<CreateForm> {

	/**
	 * the email service.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private EmailService emailService;

	/**
	 * the faces service.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private FacesService facesService;

	/**
	 * the form tracking service.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private FormTrackingService formTrackingService;

	/**
	 * the questionnaire service.
	 * 
	 * @author IanBrown
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * the pending voter registration service.
	 * @author IanBrown
	 * @since Nov 14, 2012
	 * @version Nov 14, 2012
	 */
	private PendingVoterRegistrationService pendingVoterRegistrationService;

	/**
	 * the question field service.
	 * @author IanBrown
	 * @since Nov 14, 2012
	 * @version Nov 14, 2012
	 */
	private QuestionFieldService questionFieldService;


    private CipherService cipherService;

    private PdfFormTrackService formTrackService;

	private LocalOfficialService localOfficialService;


    @Test
    public final void testCheckTrackStatus_noTrack() {
        EasyMock.expect( getFormTrackService().findFormTrack( EasyMock.anyLong() ) ).andReturn( null );
        replayAll();

        final ResponseEntity<String> responseEntity = getController().checkTrackStatus( 99l );
        assertTrue("Status should be OK", responseEntity.getStatusCode() == HttpStatus.OK );
        assertTrue("Response should contain message", responseEntity.getBody().contains("message") );
        assertTrue("Response should contain status", responseEntity.getBody().contains("status") );

        verifyAll();
    }

    @Test
    public final void testCheckTrackStatus() {
        final PdfFormTrack track = new PdfFormTrack();
        track.setId( 99l );
        track.setFormFileName( "filename" );
        track.setStatus( PdfFormTrack.IN_PROCESS );
        track.setCreated(new Date());
        EasyMock.expect( getFormTrackService().findFormTrack( track.getId() ) ).andReturn(track);
        replayAll();

        final ResponseEntity<String> responseEntity = getController().checkTrackStatus( track.getId() );
        assertTrue("Status should be OK", responseEntity.getStatusCode() == HttpStatus.OK);
        assertTrue("Response should contain id", responseEntity.getBody().contains("id") );
        assertTrue("Response should contain status", responseEntity.getBody().contains("status") );
        assertTrue("Response should not contain fileName", !responseEntity.getBody().contains("formFileName"));

        verifyAll();
    }

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CreateForm#newCreatePdf(javax.servlet.http.HttpServletRequest, Long)}
	 * for the case where there is a different user.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 18, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testHandleRequestInternal_differentUser() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		final long userId = 298l;
		EasyMock.expect(user.getId()).andReturn(userId).anyTimes();
		final MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
		createWizardContext(request, session, wizardResults, flowType);
		final OverseasUser wizardUser = createMock("WizardUser", OverseasUser.class);
		EasyMock.expect(wizardResults.getUser()).andReturn(wizardUser).atLeastOnce();
		replayAll();

		final ResponseEntity<byte[]> response = getController().newCreatePdf(request, 0l,null);
        assertTrue( "Response should return FORBIDDEN status", response.getStatusCode() == HttpStatus.FORBIDDEN );

		verifyAll();
	}

	/**
	 * Test method for
     * {@link com.bearcode.ovf.actions.questionnaire.CreateForm#newCreatePdf(javax.servlet.http.HttpServletRequest, Long)}
	 * for the case where there the email fails.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 18, 2012
	 * @version Oct 19, 2012
	 */
	@Test
	public final void testHandleRequestInternal_emailFailure() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = new OverseasUser();
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect( wizardResults.hasMailingListSignUp() ).andReturn( false ).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		final WizardContext wizardContext = createWizardContext(request, session, wizardResults, flowType);
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
        EasyMock.expect( getFacesService().findConfig( EasyMock.anyObject(String.class) )).andReturn( faceConfig ).anyTimes();
		wizardResults.setLastChangedDate((Date) EasyMock.anyObject());
		wizardResults.setDownloaded(true);
		EasyMock.expect(wizardResults.isEmailSent()).andReturn(false);
		final String username = "email@nowhere.at.all";
		sendEmail(wizardResults, username, flowType, faceConfig, true);
		wizardResults.setReportable(false);
		saveResults(wizardResults, user);
        final File tempFile = File.createTempFile("createFormTest", ".tmp");
        final Long id = 99l;
        EasyMock.expect( getFormTrackService().findTrackedFile(id) ).andReturn( tempFile );
        getCipherService().decrypt(EasyMock.anyObject(InputStream.class), EasyMock.anyObject(OutputStream.class), EasyMock.eq(id.longValue()));
        final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
        EasyMock.expect( votingAddress.getState()).andReturn("ST").anyTimes();
		EasyMock.expect( faceConfig.getRelativePrefix()).andReturn("");
		EasyMock.expect( faceConfig.getRelativePrefix()).andReturn("");
        EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress).anyTimes();
		EasyMock.expect( wizardResults.getAnswers() ).andReturn( Collections.<Answer>emptyList() ).anyTimes();
		replayAll();

		getController().newCreatePdf(request, id,null);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CreateForm#newCreatePdf(javax.servlet.http.HttpServletRequest, Long)}
	 * for the case where there is no context for the flow type.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 18, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testHandleRequestInternal_noContextForFlow() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		final FlowType flowType = FlowType.RAVA;
		session.setAttribute((String) ReflectionTestUtils.getField(SessionContextStorage.create(request), "ACTIVE_FLOW_KEY"),
				flowType);
		replayAll();

        final ResponseEntity<byte[]> response = getController().newCreatePdf(request, 0l,null);
        assertTrue( "Response should return FORBIDDEN status", response.getStatusCode() == HttpStatus.FORBIDDEN );

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CreateForm#newCreatePdf(javax.servlet.http.HttpServletRequest, Long)}
	 * for the case where there is no user and no username.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 18, 2012
	 * @version Nov 14, 2012
	 */
	@Test
	public final void testHandleRequestInternal_noUserOrUsername() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = null;
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect( wizardResults.hasMailingListSignUp() ).andReturn( false ).anyTimes();
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createWizardContext(request, session, wizardResults, flowType);
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String urlPath = "www.url://path";
        EasyMock.expect( getFacesService().findConfig( EasyMock.anyObject(String.class) )).andReturn( faceConfig ).anyTimes();
		EasyMock.expect(faceConfig.getUrlPath()).andReturn(urlPath);
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(urlPath);
		wizardResults.setFaceUrl(urlPath);
		wizardResults.setLastChangedDate((Date) EasyMock.anyObject());
		wizardResults.setDownloaded(true);
		EasyMock.expect(wizardResults.isEmailSent()).andReturn(false);
		sendEmail(wizardResults, null, flowType, faceConfig, false);
		getFormTrackingService().saveAfterThankYou(wizardContext);
		wizardResults.setReportable(false);
		saveResults(wizardResults, user);
        final File tempFile = File.createTempFile("createFormTest", ".tmp");
        final Long id = 99l;
        EasyMock.expect( getFormTrackService().findTrackedFile(id) ).andReturn( tempFile );
        getCipherService().decrypt(EasyMock.anyObject(InputStream.class), EasyMock.anyObject(OutputStream.class), EasyMock.eq(id.longValue()));
        final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
        EasyMock.expect( votingAddress.getState()).andReturn("ST").anyTimes();
        EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress).anyTimes();
		EasyMock.expect( wizardResults.getAnswers() ).andReturn( Collections.<Answer>emptyList() ).anyTimes();
		EasyMock.expect( faceConfig.getRelativePrefix()).andReturn("");
		EasyMock.expect( faceConfig.getRelativePrefix()).andReturn("");
		emailService.sendEmail(EasyMock.anyObject(WizardResults.class),EasyMock.anyObject(FaceConfig.class));
		replayAll();
		wizardContext.setCurrentFace(faceConfig);
        final ResponseEntity<byte[]> response = getController().newCreatePdf(request, id, null);
        assertEquals( "Response should return OK status", HttpStatus.OK, response.getStatusCode());

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CreateForm#newCreatePdf(javax.servlet.http.HttpServletRequest, Long)}
	 * for the case where there is no wizard context.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 18, 2012
	 * @version Jun 19, 2012
	 */
	@Test
	public final void testHandleRequestInternal_noWizardContext() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		replayAll();

        final ResponseEntity<byte[]> response = getController().newCreatePdf(request, 0l,null);
        assertTrue( "Response should return FORBIDDEN status", response.getStatusCode() == HttpStatus.FORBIDDEN );

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CreateForm#newCreatePdf(javax.servlet.http.HttpServletRequest, Long)}
	 * for the case where there the PDF generator fails.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jun 18, 2012
	 * @version Oct 19, 2012
	 */
	@Test
	public final void testHandleRequestInternal_pdfFailure() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = null;
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect( wizardResults.hasMailingListSignUp() ).andReturn( false ).anyTimes();
		final FlowType flowType = FlowType.FWAB;
		final WizardContext wizardContext = createWizardContext(request, session, wizardResults, flowType);
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn("");
        EasyMock.expect( getFacesService().findConfig( EasyMock.anyObject(String.class) )).andReturn( faceConfig ).anyTimes();
        final File tempFile = File.createTempFile("createFormTest", ".tmp");
        final Long id = 99l;
        EasyMock.expect( getFormTrackService().findTrackedFile(id) ).andReturn( tempFile );
        getCipherService().decrypt(EasyMock.anyObject(InputStream.class), EasyMock.anyObject(OutputStream.class), EasyMock.eq(id.longValue()));
		EasyMock.expectLastCall().andThrow(new RuntimeException("Expected exception"));
        final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
        EasyMock.expect( votingAddress.getState()).andReturn("ST").anyTimes();
        EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress).anyTimes();
		EasyMock.expect( wizardResults.getAnswers() ).andReturn( Collections.<Answer>emptyList() ).anyTimes();
		wizardResults.setDownloaded(false);
		EasyMock.expect(wizardResults.isDownloaded()).andReturn(false);
		EasyMock.expect( faceConfig.getRelativePrefix()).andReturn("");
		EasyMock.expect( faceConfig.getRelativePrefix()).andReturn("");
		wizardResults.setReportable(false);
		saveResults(wizardResults, user);
		replayAll();

        getController().newCreatePdf(request, id,null);

		//verifyAll();
	}


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.CreateForm#newCreatePdf(javax.servlet.http.HttpServletRequest, Long)}
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Oct 19, 2012
	 * @version Oct 19, 2012
	 */
	@Test
	public final void testHandleRequestInternal_alreadyEmailed() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = new OverseasUser();
		final long userId = 91829l;
		user.setId( userId );
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpSession session = new MockHttpSession();
		request.setSession(session);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect( wizardResults.hasMailingListSignUp() ).andReturn( false ).anyTimes();
		final FlowType flowType = FlowType.DOMESTIC_ABSENTEE;
		final WizardContext wizardContext = createWizardContext(request, session, wizardResults, flowType);
		EasyMock.expect(wizardResults.getUser()).andReturn(user).atLeastOnce();
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		EasyMock.expect(faceConfig.getRelativePrefix()).andReturn("");
        EasyMock.expect( getFacesService().findConfig( EasyMock.anyObject(String.class) )).andReturn( faceConfig ).anyTimes();
        final File tempFile = File.createTempFile("createFormTest", ".tmp");
        final Long id = 99l;
        EasyMock.expect( getFormTrackService().findTrackedFile(id) ).andReturn( tempFile );
        getCipherService().decrypt(EasyMock.anyObject(InputStream.class), EasyMock.anyObject(OutputStream.class), EasyMock.eq(id.longValue()));
        final WizardResultAddress votingAddress = createMock("VotingAddress", WizardResultAddress.class);
        EasyMock.expect( votingAddress.getState()).andReturn("ST").anyTimes();
        EasyMock.expect(wizardResults.getVotingAddress()).andReturn(votingAddress).anyTimes();
		wizardResults.setLastChangedDate( (Date) EasyMock.anyObject() );
		wizardResults.setDownloaded( true );
		EasyMock.expect(wizardResults.isEmailSent()).andReturn(true);
		EasyMock.expect( wizardResults.getAnswers() ).andReturn( Collections.<Answer>emptyList() ).anyTimes();
		EasyMock.expect( faceConfig.getRelativePrefix()).andReturn("");
		EasyMock.expect( faceConfig.getRelativePrefix()).andReturn("");
		wizardResults.setReportable(false);
		saveResults(wizardResults, user);
		replayAll();

        getController().newCreatePdf(request, id, null);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final CreateForm createController() {
		final CreateForm createForm = new CreateForm();
		ReflectionTestUtils.setField(createForm, "facesService", getFacesService());
		ReflectionTestUtils.setField(createForm, "formTrackingService", getFormTrackingService());
		ReflectionTestUtils.setField(createForm, "questionnaireService", getQuestionnaireService());
		ReflectionTestUtils.setField(createForm, "emailService", getEmailService());
        ReflectionTestUtils.setField(createForm, "cipherService", getCipherService());
        ReflectionTestUtils.setField(createForm, "formTrackService", getFormTrackService());
//		ReflectionTestUtils.setField(createForm, "localOfficialService", getLocalOfficialService());
		return createForm;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForController() {
		setFacesService(createMock("FacesService", FacesService.class));
		setFormTrackingService( createMock( "FormTrackingService", FormTrackingService.class ) );
		setQuestionnaireService( createMock( "QuestionnaireService", QuestionnaireService.class ) );
		setEmailService( createMock( "EmailService", EmailService.class ) );
        setCipherService( createMock( "cipherService", CipherService.class ) );
        setFormTrackService( createMock( "FormTrackService", PdfFormTrackService.class ) );
		setLocalOfficialService( createMock( "LocalOfficialService", LocalOfficialService.class ) );
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForController() {
		setQuestionFieldService(null);
		setPendingVoterRegistrationService(null);
		setEmailService(null);
		setQuestionnaireService(null);
		setFormTrackingService(null);
		setFacesService(null);
        setCipherService(null);
        setFormTrackService(null);
	}

	/**
	 * Creates a PDF generator.
	 * 
	 * @author IanBrown
	 * @param response
	 *            the response.
	 * @param wizardContext
	 *            the wizard context.
	 * @param faceConfig
	 *            the face configuration.
	 * @return the PDF generator.
	 * @throws PdfGeneratorException
	 *             if there is a problem setting up the PDF generator.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private PdfGenerator createPdfGenerator(final MockHttpServletResponse response, final WizardContext wizardContext,
			final FaceConfig faceConfig) throws PdfGeneratorException {
		EasyMock.expect(getFacesService().findConfig((String) EasyMock.anyObject())).andReturn(faceConfig);
		final PdfGenerator flowPdfGenerator = createMock("FlowPdfGenerator", PdfGenerator.class);
		return flowPdfGenerator;
	}

	/**
	 * Creates a wizard context wrapping the results.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param session
	 *            the session.
	 * @param wizardResults
	 *            the wizard results.
	 * @param flowType
	 *            the flow type.
	 * @return the wizard context.
	 * @since Jun 19, 2012
	 * @version Nov 14, 2012
	 */
	private WizardContext createWizardContext(final MockHttpServletRequest request, final MockHttpSession session,
			final WizardResults wizardResults, final FlowType flowType) {
		session.setAttribute((String) ReflectionTestUtils.getField(SessionContextStorage.create(request), "ACTIVE_FLOW_KEY"),
				flowType);
		final String flowKey = flowKey(flowType);
		EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType).anyTimes();
		final WizardContext wizardContext = new WizardContext(wizardResults);
		session.setAttribute(flowKey, wizardContext);
		return wizardContext;
	}

	/**
	 * Creates a flow key for the input flow type.
	 * 
	 * @author IanBrown
	 * @param flowType
	 *            the flow type.
	 * @return the flow key.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private String flowKey(final FlowType flowType) {
		return String.format("%s.%s", WizardContext.class.getName(), flowType.name());
	}

	/**
	 * Gets the email service.
	 * 
	 * @author IanBrown
	 * @return the email service.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private EmailService getEmailService() {
		return emailService;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the form tracking service.
	 * 
	 * @author IanBrown
	 * @return the form tracking service.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private FormTrackingService getFormTrackingService() {
		return formTrackingService;
	}


	/**
	 * Gets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	/**
	 * Sets up the mocks to save the wizard results.
	 * 
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @param user
	 *            the user.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private void saveResults(final WizardResults wizardResults, final OverseasUser user) {
		final WizardResults temporary = createMock("Temporary", WizardResults.class);
		EasyMock.expect(wizardResults.createTemporary()).andReturn(temporary);
		getQuestionnaireService().saveWizardResults(wizardResults);
		wizardResults.copyFromTemporary(temporary);
	}

	/**
	 * Sets up the mocks to send an email.
	 * 
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @param username
	 *            the username (email address).
	 * @param flowType
	 *            the flow type.
	 * @param faceConfig
	 *            the face configuration.
	 * @param emailFailure
	 *            <code>true</code> if the email should fail on an error, <code>false</code> otherwise.
	 * @throws EmailException
	 *             if there is a problem sending the email.
	 * @since Jun 19, 2012
	 * @version Nov 14, 2012
	 */
	private void sendEmail(final WizardResults wizardResults, final String username, final FlowType flowType,
			final FaceConfig faceConfig, final boolean emailFailure) throws EmailException {
		if (username != null && !username.trim().isEmpty()) {
			final WizardResultPerson name = createMock("Name", WizardResultPerson.class);
			final String firstName = "First";
			final String relativePrefix = "faceConfig/";
			EasyMock.expect(faceConfig.getRelativePrefix()).andReturn(relativePrefix);
			final String approvedFileName = "approved.file";
			emailService.sendEmail(EasyMock.anyObject(WizardResults.class),EasyMock.anyObject(FaceConfig.class));
			if (emailFailure) {
				EasyMock.expectLastCall().andThrow(new EmailException("Expected exception"));
			}
			LocalOfficial leo = new LocalOfficial();
			VotingRegion region = new VotingRegion();
		}
		wizardResults.setEmailSent(!emailFailure);
	}

	/**
	 * Sets the email service.
	 * 
	 * @author IanBrown
	 * @param emailService
	 *            the email service to set.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
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
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private void setFacesService(final FacesService facesService) {
		this.facesService = facesService;
	}

	/**
	 * Sets the form tracking service.
	 * 
	 * @author IanBrown
	 * @param formTrackingService
	 *            the form tracking service to set.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private void setFormTrackingService(final FormTrackingService formTrackingService) {
		this.formTrackingService = formTrackingService;
	}

	/**
	 * Sets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @param questionnaireService
	 *            the questionnaire service to set.
	 * @since Jun 19, 2012
	 * @version Jun 19, 2012
	 */
	private void setQuestionnaireService(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	/**
	 * Gets the pending voter registration service.
	 * @author IanBrown
	 * @return the pending voter registration service.
	 * @since Nov 14, 2012
	 * @version Nov 14, 2012
	 */
	private PendingVoterRegistrationService getPendingVoterRegistrationService() {
		return pendingVoterRegistrationService;
	}

	/**
	 * Sets the pending voter registration service.
	 * @author IanBrown
	 * @param pendingVoterRegistrationService the pending voter registration service to set.
	 * @since Nov 14, 2012
	 * @version Nov 14, 2012
	 */
	private void setPendingVoterRegistrationService(final PendingVoterRegistrationService pendingVoterRegistrationService) {
		this.pendingVoterRegistrationService = pendingVoterRegistrationService;
	}

	/**
	 * Gets the question field service.
	 * @author IanBrown
	 * @return the question field service.
	 * @since Nov 14, 2012
	 * @version Nov 14, 2012
	 */
	private QuestionFieldService getQuestionFieldService() {
		return questionFieldService;
	}

	/**
	 * Sets the question field service.
	 * @author IanBrown
	 * @param questionFieldService the question field service to set.
	 * @since Nov 14, 2012
	 * @version Nov 14, 2012
	 */
	private void setQuestionFieldService(final QuestionFieldService questionFieldService) {
		this.questionFieldService = questionFieldService;
	}

    public CipherService getCipherService() {
        return cipherService;
    }

    public void setCipherService(final CipherService cipherService) {
        this.cipherService = cipherService;
    }

    public PdfFormTrackService getFormTrackService() {
        return formTrackService;
    }

    public void setFormTrackService(final PdfFormTrackService formTrackService) {
        this.formTrackService = formTrackService;
    }

	public LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	public void setLocalOfficialService( LocalOfficialService localOfficialService ) {
		this.localOfficialService = localOfficialService;
	}
}
